package com.ateam.calmate.event.command.service.gacha;

import com.ateam.calmate.event.command.dto.gacha.GachaDrawResult;
import com.ateam.calmate.event.command.entity.gacha.GachaPrizeEntity;
import com.ateam.calmate.event.command.port.PointsPort;
import com.ateam.calmate.event.command.repository.gacha.GachaPrizeRepository;
import com.ateam.calmate.event.command.repository.gacha.GachaSharedBoardRepository;
import com.ateam.calmate.event.enums.CellStatus;
import com.ateam.calmate.member.command.entity.Point;
import com.ateam.calmate.member.command.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GachaCommandService {

    private final JdbcTemplate jdbc;      // 원자적 UPDATE에 깔끔
    private final SimpMessagingTemplate broker;
    private final PointsPort pointsPort;
    private final GachaSharedBoardRepository boardRepository;
    private final GachaPrizeRepository prizeRepository;
    private final GachaBoardCommandService boardCommandService;
    private final com.ateam.calmate.event.command.repository.gacha.GachaCommandRepository gachaEventRepository;
    private final PointRepository pointRepository;

    @Value("${gacha.points.cost:100}")
    private int gachaCost;

    @Transactional
    public GachaDrawResult draw(Long eventId, Long memberId, Long boardCellId) {
        if (boardCellId == null) {
            throw new IllegalArgumentException("boardCellId must not be null");
        }

        // 0) 포인트 차감 (가챠 실행 비용)
        pointsPort.deductPoints(memberId, gachaCost, "GACHA_DRAW");

        // 0-1) 포인트 차감 이력 기록
        recordPointHistory(memberId, eventId, gachaCost, Point.Distinction.USE);

        // 1) 원자적 차감 시도
        int updated = jdbc.update("""
            UPDATE gacha_prize_inventory
            SET status='USED', used_by=?, used_at=NOW()
            WHERE id = (
              SELECT id FROM gacha_prize_inventory
              WHERE event_id=? AND status='READY'
              ORDER BY id ASC
              LIMIT 1
            ) AND status='READY'
            """, ps -> {
            ps.setLong(1, memberId);
            ps.setLong(2, eventId);
        });

        if (updated == 0) {
            // 품절 - 자동으로 재고 충전
            refillInventory(eventId);

            // 재충전 후 다시 시도
            updated = jdbc.update("""
                UPDATE gacha_prize_inventory
                SET status='USED', used_by=?, used_at=NOW()
                WHERE id = (
                  SELECT id FROM gacha_prize_inventory
                  WHERE event_id=? AND status='READY'
                  ORDER BY id ASC
                  LIMIT 1
                ) AND status='READY'
                """, ps -> {
                ps.setLong(1, memberId);
                ps.setLong(2, eventId);
            });

            if (updated == 0) {
                // 재충전 후에도 실패하면 에러
                GachaDrawResult out = GachaDrawResult.builder()
                        .success(false).reason("REFILL_FAILED")
                        .eventId(eventId).memberId(memberId)
                        .type("DRAW_RESULT").remainCount(0)
                        .build();
                broker.convertAndSend("/topic/gacha/" + eventId, out);
                return out;
            }
        }

        // 2) 방금 사용 처리된 아이템 정보 읽기
        var used = jdbc.queryForMap("""
            SELECT id, prize_tier FROM gacha_prize_inventory
            WHERE used_by=? AND event_id=? 
            ORDER BY used_at DESC LIMIT 1
            """, memberId, eventId);
        Long inventoryId = ((Number)used.get("id")).longValue();
        String prizeTier  = (String) used.get("prize_tier");

        // 3) 사용자가 선택한 셀을 OPENED 상태로 변경
        var cell = boardRepository.findById(boardCellId)
                .orElseThrow(() -> new IllegalStateException("Cell not found: " + boardCellId));

        if (!Objects.equals(cell.getGachaEventId(), eventId)) {
            throw new IllegalArgumentException("Cell does not belong to this event");
        }
        if (cell.getStatus() == CellStatus.OPENED) {
            throw new IllegalStateException("Cell already opened: " + boardCellId);
        }

        Long actualPrizeId = cell.getGachaPrizeId();

        // 셀을 OPENED 상태로 변경
        cell.setStatus(CellStatus.OPENED);
        cell.setOpenedByMemberId(memberId);
        cell.setOpenedAt(java.time.LocalDateTime.now());
        boardRepository.save(cell);

        // 4) prize 정보 조회 (로그 기록 및 1등 확인에 사용)
        GachaPrizeEntity prize = prizeRepository.findById(actualPrizeId).orElse(null);

        // 5) 로그 기록
        jdbc.update("""
            INSERT INTO gacha_draw_log(member_id, gacha_event_id, gacha_shared_board_id, board_version, prize_id, prize_rank, result_code)
            VALUES (?,?,?,?,?,?,?)
            """, memberId, eventId, cell.getId(), cell.getBoardVersion(), actualPrizeId, prize != null ? prize.getRank() : 0, "SUCCESS");

        // 6) 포인트 경품인 경우 포인트 지급
        if ("100 포인트".equals(prizeTier)) {
            // 포인트 지급
            pointsPort.addPoints(memberId, 100, "GACHA_REWARD");

            // 포인트 지급 이력 기록
            recordPointHistory(memberId, eventId, 100, Point.Distinction.EARN);
        }

        // 7) 1등(최고 등급) 경품을 뽑았는지 확인하고, 뽑았다면 보드 리셋
        if (prize != null) {
            // 최고 등급(가장 작은 rank)인지 확인
            Integer topRank = prizeRepository.findFirstByEvent_IdOrderByRankAsc(eventId)
                    .map(GachaPrizeEntity::getRank)
                    .orElse(null);

            if (topRank != null && topRank.equals(prize.getRank())) {
                // 1등을 뽑았으므로 새 보드 생성 (handleTopRankResetIfNeeded 로직 사용)
                boardCommandService.handleTopRankResetIfNeeded(cell, prize);
            }
        }

        // 8) 남은 재고 카운트
        Long remain = jdbc.queryForObject("""
            SELECT COUNT(*) FROM gacha_prize_inventory
            WHERE event_id=? AND status='READY'
            """, Long.class, eventId);

        // 5) 브로드캐스트 (모든 클라이언트에게 최신 상태 push)
        GachaDrawResult out = GachaDrawResult.builder()
                .success(true).reason("OK")
                .eventId(eventId).memberId(memberId)
                .inventoryId(inventoryId).prizeTier(prizeTier)
                .remainCount(remain).type("DRAW_RESULT")
                .cellId(cell.getId())
                .row(cell.getRow())
                .col(cell.getCol())
                .build();

        broker.convertAndSend("/topic/gacha/" + eventId, out);
        return out;
    }

    /**
     * 재고(gacha_prize_inventory)를 자동으로 재충전하는 메서드
     * 모든 경품이 소진되면 새로운 보드를 생성하고 재고를 다시 채움
     * 고정된 경품 목록으로 재고를 채웁니다.
     */
    private void refillInventory(Long eventId) {
        // 고정 경품 목록 및 수량 정의
        String[][] fixedPrizes = {
            {"츄잉껌", "10"},      // 경품명, 수량
            {"츄파춥스", "15"},
            {"100 포인트", "25"},
            {"꽝", "50"}
        };

        // gacha_prize_inventory에 고정 경품 재고 추가
        for (String[] prize : fixedPrizes) {
            String prizeName = prize[0];
            int count = Integer.parseInt(prize[1]);

            for (int i = 0; i < count; i++) {
                jdbc.update("""
                    INSERT INTO gacha_prize_inventory
                    (event_id, prize_tier, status)
                    VALUES (?, ?, 'READY')
                    """,
                    eventId,
                    prizeName
                );
            }
        }

        // 이벤트의 모든 경품 조회 (보드 생성용)
        List<GachaPrizeEntity> prizes = prizeRepository.findAllByEvent_IdOrderByRankAsc(eventId);
        if (prizes.isEmpty()) {
            throw new IllegalStateException("No prizes configured for event: " + eventId);
        }

        // 3) 새로운 보드 버전 생성 (모든 셀을 COVERED로 리셋)
        // 현재 보드 버전을 가져와서 COVERED 셀 개수 확인
        Long coveredCount = jdbc.queryForObject("""
            SELECT COUNT(*) FROM gacha_shared_board
            WHERE gacha_event_id = ? AND status = 'COVERED'
            """, Long.class, eventId);

        // COVERED 셀이 부족하면 새 보드 버전 생성
        if (coveredCount == null || coveredCount < 10) {
            // GachaEventEntity를 조회하여 새 보드 버전 생성
            var event = gachaEventRepository.findById(eventId).orElse(null);

            if (event != null) {
                // 새 보드 버전 생성
                int nextVersion = (event.getCurrentBoardVersion() == null ? 1 : event.getCurrentBoardVersion() + 1);

                // createNextBoardVersion과 동일한 로직으로 새 보드 생성
                List<Long> prizeIds = new java.util.ArrayList<>();
                for (GachaPrizeEntity prize : prizes) {
                    var quantity = prize.getQuantity();
                    int count = (quantity != null && quantity.getCount() != null) ? quantity.getCount() : 10;
                    for (int i = 0; i < count && prizeIds.size() < 100; i++) {
                        prizeIds.add(prize.getId());
                    }
                }

                java.util.Collections.shuffle(prizeIds);

                int idx = 0;
                for (int row = 1; row <= 10; row++) {
                    for (int col = 1; col <= 10; col++) {
                        Long prizeId = idx < prizeIds.size() ? prizeIds.get(idx) : prizes.get(0).getId();
                        jdbc.update("""
                            INSERT INTO gacha_shared_board
                            (gacha_event_id, board_version, row, col, gacha_prize_id, status, version, created_at, updated_at)
                            VALUES (?, ?, ?, ?, ?, 'COVERED', 0, NOW(), NOW())
                            """,
                            eventId, nextVersion, row, col, prizeId
                        );
                        idx++;
                    }
                }

                // 보드 버전 업데이트
                jdbc.update("UPDATE gacha_event SET current_board_version = ? WHERE id = ?", nextVersion, eventId);
            }
        }
    }

    /**
     * 포인트 이력 기록
     */
    private void recordPointHistory(Long memberId, Long eventId, int amount, Point.Distinction distinction) {
        Point history = new Point();
        history.setMemberId(memberId);
        history.setPoint(amount);
        history.setDistinction(distinction);
        history.setGachaEventId(eventId);
        pointRepository.save(history);
    }
}
