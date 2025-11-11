package com.ateam.calmate.event.command.service.bingo;

import com.ateam.calmate.event.command.entity.bingo.BingoBoardEntity;
import com.ateam.calmate.event.command.entity.bingo.BingoCellEntity;
import com.ateam.calmate.event.command.repository.bingo.BingoBoardCommandRepository;
import com.ateam.calmate.event.command.repository.bingo.BingoCellCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BingoBoardFactoryService {

    private final BingoBoardCommandRepository boardRepo;
    private final BingoCellCommandRepository cellRepo;

    // ✅ 5x5 라벨 표 (0-based 인덱스)
    private static final String[][] LABELS_5X5 = {
            { "물 2L 마시기", "스트레칭 10분", "계단 이용", "운동 일기 쓰기", "운동 기록 하기" },
            { "식단 기록 하기", "흡연 하지 않기", "단백질 식품 한 가지 이상 섭취하기 (닭가슴살/콩/두부 등)", "20분 산책하기", "1분 플랭크 버티기" },
            { "푸쉬업 10번", "설탕 들어간 음료 대신 제로/물 선택하기", "과식하지 않기 (포만감 80% 선에서 식사 종료)", "패스트푸드 안 먹기", "8,000보 이상 걷기 또는 스마트워치/앱 기록" },
            { "체중 측정하고 기록하기", "샐러드 한 끼 섭취하기", "식사 전 물 한 컵 마시기", "홈트 영상 따라하기 (10~20분)", "스쿼트 30회" },
            { "런지 좌/우 20회", "목/거북목 스트레칭 5분", "허리 스트레칭 5분", "식사 시 씹는 횟수 20회 이상", "물 대신 따뜻한 차 마시기" }
    };

    /** 현재 월 보드가 없으면 생성(셀 시드 포함), 있으면 반환 */
    @Transactional
    public BingoBoardEntity ensureMonthlyBoard(Long memberId, YearMonth ym, int size, String title) {
        var start = ym.atDay(1);
        return boardRepo.findByMemberIdAndStartDate(memberId, start)
                .orElseGet(() -> createMonthlyBoardWithSeeds(memberId, ym, size, title));
    }

    private BingoBoardEntity createMonthlyBoardWithSeeds(Long memberId, YearMonth ym, int size, String title) {
        var start = ym.atDay(1);
        var end   = ym.atEndOfMonth();
        try {
            var board = BingoBoardEntity.builder()
                    .memberId(memberId)
                    .title(title != null ? title : (ym + " Bingo"))
                    .size(size)
                    .startDate(start)
                    .endDate(end)
                    .createdAt(java.time.LocalDateTime.now())
                    .build();
            var saved = boardRepo.save(board);

            List<BingoCellEntity> cells = new ArrayList<>(size * size);

            // row/col 1부터 시작하도록 변경
            for (int r = 1; r <= size; r++) {
                for (int c = 1; c <= size; c++) {
                    String label = resolveLabel(r, c, size);
                    cells.add(BingoCellEntity.builder()
                            .board(saved)
                            .row(r)
                            .col(c)
                            .label(label)
                            .checked(false)
                            .build());
                }
            }
            cellRepo.saveAll(cells);
            return saved;

        } catch (DataIntegrityViolationException dup) {
            // 동시성: 유니크 충돌 → 이미 생성된 보드를 다시 조회
            return boardRepo.findByMemberIdAndStartDate(memberId, start)
                    .orElseThrow(() -> dup);
        }
    }

    /** size==5일 때 고정 라벨, 그 외는 기본 라벨 */
    private String resolveLabel(int r, int c, int size) {
        if (size == 5 && r >= 1 && r <= 5 && c >= 1 && c <= 5) {
            return LABELS_5X5[r - 1][c - 1];  // ✅ 1-based 인덱스에 맞춰 변환
        }
        return "r" + r + "c" + c;
    }
}