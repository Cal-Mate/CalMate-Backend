package com.ateam.calmate.event.command.service.gacha;

import com.ateam.calmate.event.command.entity.gacha.*;
import com.ateam.calmate.event.command.repository.gacha.GachaCommandRepository;
import com.ateam.calmate.event.command.repository.gacha.GachaDrawLogRepository;
import com.ateam.calmate.event.command.repository.gacha.GachaPrizeRepository;
import com.ateam.calmate.event.command.repository.gacha.GachaSharedBoardRepository;
import com.ateam.calmate.event.enums.CellStatus;
import com.ateam.calmate.event.enums.PolicyType;
import com.ateam.calmate.event.query.dto.gacha.GachaDrawLogDTO;
import com.ateam.calmate.event.query.dto.gacha.GachaSharedBoardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class GachaBoardCommandService {

    private static final int BOARD_DIMENSION = 10;
    private static final int BOARD_CELL_COUNT = BOARD_DIMENSION * BOARD_DIMENSION;

    private final GachaSharedBoardRepository boardRepository;
    private final GachaPrizeRepository prizeRepository;
    private final GachaDrawLogRepository drawLogRepository;
    private final GachaCommandRepository eventRepository;

    @Transactional
    public GachaSharedBoardDTO openCell(Long cellId, Long memberId, Long prizeId) {
        Assert.notNull(cellId, "cellId must not be null");
        Assert.notNull(memberId, "memberId must not be null");
        Assert.notNull(prizeId, "prizeId must not be null");

        GachaSharedBoardEntity cell = boardRepository.findById(cellId)
                .orElseThrow(() -> new IllegalArgumentException("Board cell not found: " + cellId));

        if (cell.getStatus() == CellStatus.OPENED) {
            throw new IllegalStateException("Cell already opened: " + cellId);
        }

        GachaPrizeEntity prize = prizeRepository.findById(prizeId)
                .orElseThrow(() -> new IllegalArgumentException("Prize not found: " + prizeId));
        if (!Objects.equals(prize.getEvent().getId(), cell.getGachaEventId())) {
            throw new IllegalArgumentException("Prize does not belong to this event");
        }

        cell.open(memberId, prizeId);
        handleTopRankResetIfNeeded(cell, prize);
        return toDto(cell);
    }

    @Transactional
    public GachaDrawLogDTO recordDraw(Long memberId, Long boardCellId, Long prizeId) {
        Assert.notNull(memberId, "memberId must not be null");
        Assert.notNull(boardCellId, "boardCellId must not be null");
        Assert.notNull(prizeId, "prizeId must not be null");

        GachaSharedBoardEntity cell = boardRepository.findById(boardCellId)
                .orElseThrow(() -> new IllegalArgumentException("Board cell not found: " + boardCellId));

        GachaPrizeEntity prize = prizeRepository.findById(prizeId)
                .orElseThrow(() -> new IllegalArgumentException("Prize not found: " + prizeId));

        GachaDrawLogEntity log = GachaDrawLogEntity.builder()
                .memberId(memberId)
                .gachaEventId(cell.getGachaEventId())
                .gachaSharedBoardId(cell.getId())
                .boardVersion(cell.getBoardVersion())
                .prizeId(prizeId)
                .prizeRank(prize.getRank())
                .resultCode("SUCCESS")
                .build();

        return toDto(drawLogRepository.save(log));
    }

    @Transactional(readOnly = true)
    public Page<GachaDrawLogDTO> findMemberHistory(Long memberId, Long eventId, Pageable pageable) {
        Assert.notNull(memberId, "memberId must not be null");
        Page<GachaDrawLogEntity> page = (eventId == null)
                ? drawLogRepository.findByMemberIdOrderByCreatedAtDesc(memberId, pageable)
                : drawLogRepository.findByMemberIdAndGachaEventIdOrderByCreatedAtDesc(memberId, eventId, pageable);
        return page.map(this::toDto);
    }

    private void handleTopRankResetIfNeeded(GachaSharedBoardEntity cell, GachaPrizeEntity hitPrize) {
        GachaEventEntity event = eventRepository.findById(cell.getGachaEventId())
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + cell.getGachaEventId()));

        if (event.getResetPolicy() == null || event.getResetPolicy().getPolicyType() != PolicyType.TOP_RANK) {
            return;
        }

        Integer topRank = prizeRepository.findFirstByEvent_IdOrderByRankAsc(event.getId())
                .map(GachaPrizeEntity::getRank)
                .orElse(null);

        if (topRank == null || !topRank.equals(hitPrize.getRank())) {
            return;
        }

        createNextBoardVersion(event);
    }

    private void createNextBoardVersion(GachaEventEntity event) {
        int nextVersion = (event.getCurrentBoardVersion() == null ? 1 : event.getCurrentBoardVersion() + 1);
        List<GachaPrizeEntity> prizes = prizeRepository.findAllByEvent_IdOrderByRankAsc(event.getId());
        if (prizes.isEmpty()) {
            throw new IllegalStateException("No prizes configured for event: " + event.getId());
        }

        List<GachaSharedBoardEntity> newCells = new ArrayList<>(BOARD_CELL_COUNT);
        for (int row = 1; row <= BOARD_DIMENSION; row++) {
            for (int col = 1; col <= BOARD_DIMENSION; col++) {
                Long prizeId = pickPrizeId(prizes);
                newCells.add(GachaSharedBoardEntity.coveredCell(event.getId(), nextVersion, row, col, prizeId));
            }
        }

        boardRepository.saveAll(newCells);
        event.setCurrentBoardVersion(nextVersion);
    }

    private Long pickPrizeId(List<GachaPrizeEntity> prizes) {
        int totalWeight = prizes.stream()
                .mapToInt(this::prizeWeight)
                .sum();
        if (totalWeight <= 0) {
            // fallback: equal probability
            return prizes.get(ThreadLocalRandom.current().nextInt(prizes.size())).getId();
        }

        int target = ThreadLocalRandom.current().nextInt(totalWeight);
        for (GachaPrizeEntity prize : prizes) {
            target -= prizeWeight(prize);
            if (target < 0) {
                return prize.getId();
            }
        }
        return prizes.get(prizes.size() - 1).getId();
    }

    private int prizeWeight(GachaPrizeEntity prize) {
        GachaQuantityEntity quantity = prize.getQuantity();
        if (quantity != null && quantity.getCount() != null && quantity.getCount() > 0) {
            return Math.min(quantity.getCount(), 100);
        }
        int rank = prize.getRank() == null ? 10 : prize.getRank();
        // Higher rank value => lower weight
        return Math.max(1, 11 - Math.min(rank, 10));
    }

    private GachaSharedBoardDTO toDto(GachaSharedBoardEntity entity) {
        return GachaSharedBoardDTO.builder()
                .id(entity.getId())
                .gachaEventId(entity.getGachaEventId())
                .boardVersion(entity.getBoardVersion())
                .row(entity.getRow())
                .col(entity.getCol())
                .gachaPrizeId(entity.getGachaPrizeId())
                .status(entity.getStatus())
                .openedByMemberId(entity.getOpenedByMemberId())
                .openedAt(entity.getOpenedAt())
                .version(entity.getVersion())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private GachaDrawLogDTO toDto(GachaDrawLogEntity entity) {
        return GachaDrawLogDTO.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .memberId(entity.getMemberId())
                .gachaSharedBoardId(entity.getGachaSharedBoardId())
                .boardVersion(entity.getBoardVersion())
                .gachaPrizeId(entity.getPrizeId())
                .build();
    }
}
