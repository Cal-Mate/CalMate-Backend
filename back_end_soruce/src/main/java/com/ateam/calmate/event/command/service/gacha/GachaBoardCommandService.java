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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GachaBoardCommandService {

    private static final int BOARD_DIMENSION = 10;
    private static final int BOARD_CELL_COUNT = BOARD_DIMENSION * BOARD_DIMENSION;
    private static final List<String> RARITY_ORDER = List.of("common", "rare", "epic", "legendary");

    private static final Logger log = LoggerFactory.getLogger(GachaBoardCommandService.class);

    private final GachaSharedBoardRepository boardRepository;
    private final GachaPrizeRepository prizeRepository;
    private final GachaDrawLogRepository drawLogRepository;
    private final GachaCommandRepository eventRepository;
    private final ObjectMapper objectMapper;

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

        return toDto(drawLogRepository.save(log), prize);
    }

    @Transactional(readOnly = true)
    public GachaDrawHistoryResult findMemberHistory(Long memberId, Long eventId, Pageable pageable) {
        Assert.notNull(memberId, "memberId must not be null");
        Page<GachaDrawLogEntity> page = (eventId == null)
                ? drawLogRepository.findByMemberIdOrderByCreatedAtDesc(memberId, pageable)
                : drawLogRepository.findByMemberIdAndGachaEventIdOrderByCreatedAtDesc(memberId, eventId, pageable);
        Map<Long, GachaPrizeEntity> prizeById = resolvePrizes(page);
        Page<GachaDrawLogDTO> dtoPage = page.map(entity -> toDto(entity, prizeById.get(entity.getPrizeId())));
        Map<String, Long> rarityStats = computeRarityStats(memberId, eventId);
        return new GachaDrawHistoryResult(dtoPage, rarityStats);
    }

    @Transactional
    public void handleTopRankResetIfNeeded(GachaSharedBoardEntity cell, GachaPrizeEntity hitPrize) {
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

        if (log.isInfoEnabled()) {
            log.info("Top rank prize ({}) drawn! Resetting board for event {}", hitPrize.getRank(), event.getId());
        }

        createNextBoardVersion(event);
    }

    @Transactional
    public void ensureInitialBoardExists(Long eventId) {
        GachaEventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));

        // current_board_version이 null이거나 0이면 초기 보드 생성
        Integer currentVersion = event.getCurrentBoardVersion();
        if (currentVersion == null || currentVersion == 0) {
            if (log.isInfoEnabled()) {
                log.info("Creating initial board for event {}", eventId);
            }
            createNextBoardVersion(event);
            return;
        }

        // current_board_version은 설정되어 있지만 실제 보드 데이터가 없는 경우만 생성
        long cellCount = boardRepository.countByGachaEventIdAndBoardVersion(eventId, currentVersion);

        if (log.isDebugEnabled()) {
            log.debug("Board check for event {} version {}: found {} cells (expected {})",
                    eventId, currentVersion, cellCount, BOARD_CELL_COUNT);
        }

        if (cellCount >= BOARD_CELL_COUNT) {
            // 완전한 보드가 이미 존재함 - 아무것도 하지 않음
            if (log.isDebugEnabled()) {
                log.debug("Complete board already exists for event {} version {}. Skipping creation.",
                        eventId, currentVersion);
            }
            return;
        }

        // 보드가 이미 일부 열려있는 경우 (0 < cellCount < BOARD_CELL_COUNT) - 아무것도 하지 않음
        if (cellCount > 0 && cellCount < BOARD_CELL_COUNT) {
            if (log.isDebugEnabled()) {
                log.debug("Board is already in use for event {} version {} with {} cells opened. Skipping recreation.",
                        eventId, currentVersion, BOARD_CELL_COUNT - cellCount);
            }
            return;
        }

        // cellCount == 0: 보드 데이터가 전혀 없으면 현재 버전으로 보드 생성
        if (log.isInfoEnabled()) {
            log.info("Creating board for event {} version {}", eventId, currentVersion);
        }
        createBoardForVersion(event, currentVersion);
    }

    private void createBoardForVersion(GachaEventEntity event, int version) {
        List<GachaPrizeEntity> prizes = prizeRepository.findAllByEvent_IdOrderByRankAsc(event.getId());
        if (prizes.isEmpty()) {
            throw new IllegalStateException("No prizes configured for event: " + event.getId());
        }

        List<Long> prizePool = buildPrizePoolFromQuantities(prizes);
        boolean usingQuantityPool = !prizePool.isEmpty();
        if (usingQuantityPool) {
            Collections.shuffle(prizePool);
            if (prizePool.size() < BOARD_CELL_COUNT && log.isWarnEnabled()) {
                log.warn("Gacha board seed shortage for event {}. Filling remaining {} cells with weighted fallback.",
                        event.getId(), BOARD_CELL_COUNT - prizePool.size());
            }
        }

        List<GachaSharedBoardEntity> newCells = new ArrayList<>(BOARD_CELL_COUNT);
        int poolCursor = 0;
        for (int row = 1; row <= BOARD_DIMENSION; row++) {
            for (int col = 1; col <= BOARD_DIMENSION; col++) {
                Long prizeId;
                if (usingQuantityPool && poolCursor < prizePool.size()) {
                    prizeId = prizePool.get(poolCursor++);
                } else {
                    prizeId = pickPrizeId(prizes);
                }
                newCells.add(GachaSharedBoardEntity.coveredCell(event.getId(), version, row, col, prizeId));
            }
        }

        boardRepository.saveAll(newCells);
        if (log.isInfoEnabled()) {
            log.info("Created gacha board for event {} version {} with {} cells",
                    event.getId(), version, newCells.size());
        }
    }

    private void createNextBoardVersion(GachaEventEntity event) {
        int nextVersion = (event.getCurrentBoardVersion() == null ? 1 : event.getCurrentBoardVersion() + 1);
        List<GachaPrizeEntity> prizes = prizeRepository.findAllByEvent_IdOrderByRankAsc(event.getId());
        if (prizes.isEmpty()) {
            throw new IllegalStateException("No prizes configured for event: " + event.getId());
        }

        List<Long> prizePool = buildPrizePoolFromQuantities(prizes);
        boolean usingQuantityPool = !prizePool.isEmpty();
        if (usingQuantityPool) {
            Collections.shuffle(prizePool);
            if (prizePool.size() < BOARD_CELL_COUNT && log.isWarnEnabled()) {
                log.warn("Gacha board seed shortage for event {}. Filling remaining {} cells with weighted fallback.",
                        event.getId(), BOARD_CELL_COUNT - prizePool.size());
            }
        }

        List<GachaSharedBoardEntity> newCells = new ArrayList<>(BOARD_CELL_COUNT);
        int poolCursor = 0;
        for (int row = 1; row <= BOARD_DIMENSION; row++) {
            for (int col = 1; col <= BOARD_DIMENSION; col++) {
                Long prizeId;
                if (usingQuantityPool && poolCursor < prizePool.size()) {
                    prizeId = prizePool.get(poolCursor++);
                } else {
                    prizeId = pickPrizeId(prizes);
                }
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

    private List<Long> buildPrizePoolFromQuantities(List<GachaPrizeEntity> prizes) {
        List<Long> pool = new ArrayList<>(BOARD_CELL_COUNT);
        for (GachaPrizeEntity prize : prizes) {
            GachaQuantityEntity quantity = prize.getQuantity();
            if (quantity == null || quantity.getCount() == null || quantity.getCount() <= 0) {
                continue;
            }
            int slots = Math.min(quantity.getCount(), BOARD_CELL_COUNT - pool.size());
            for (int i = 0; i < slots; i++) {
                pool.add(prize.getId());
            }
            if (pool.size() == BOARD_CELL_COUNT) {
                break;
            }
        }
        if (pool.isEmpty() && log.isInfoEnabled()) {
            log.info("No gacha_quantity configuration detected. Falling back to weighted random board seed.");
        }
        return pool;
    }

    private GachaDrawLogDTO toDto(GachaDrawLogEntity entity, GachaPrizeEntity prize) {
        return GachaDrawLogDTO.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .memberId(entity.getMemberId())
                .gachaEventId(entity.getGachaEventId())
                .gachaSharedBoardId(entity.getGachaSharedBoardId())
                .boardVersion(entity.getBoardVersion())
                .gachaPrizeId(entity.getPrizeId())
                .prizeRank(entity.getPrizeRank())
                .prizeName(prize != null ? prize.getName() : null)
                .prizeType(prize != null ? prize.getPrizeType() : null)
                .prizePayload(prize != null ? parsePayloadJson(prize.getPayloadJson()) : null)
                .rarity(resolveRarityByRank(entity.getPrizeRank()))
                .build();
    }

    private Map<String, Object> parsePayloadJson(String payloadJson) {
        if (payloadJson == null || payloadJson.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(payloadJson, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            log.warn("Failed to parse payloadJson: {}", payloadJson, e);
            return null;
        }
    }

    private Map<Long, GachaPrizeEntity> resolvePrizes(Page<GachaDrawLogEntity> page) {
        if (page.isEmpty()) {
            return Collections.emptyMap();
        }
        Set<Long> prizeIds = page.getContent().stream()
                .map(GachaDrawLogEntity::getPrizeId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (prizeIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return prizeRepository.findAllById(prizeIds).stream()
                .collect(Collectors.toMap(GachaPrizeEntity::getId, Function.identity()));
    }

    private Map<String, Long> computeRarityStats(Long memberId, Long eventId) {
        Map<String, Long> stats = new LinkedHashMap<>();
        RARITY_ORDER.forEach(rarity -> stats.put(rarity, 0L));

        drawLogRepository.countByMemberAndEventGroupByRank(memberId, eventId)
                .forEach(row -> {
                    Integer rank = row[0] instanceof Number ? ((Number) row[0]).intValue() : null;
                    long count = row[1] instanceof Number ? ((Number) row[1]).longValue() : 0L;
                    String rarity = resolveRarityByRank(rank);
                    stats.merge(rarity, count, Long::sum);
                });

        return stats;
    }

    private String resolveRarityByRank(Integer rank) {
        if (rank == null) {
            return "common";
        }
        if (rank == 1) return "legendary";
        if (rank == 2) return "epic";
        if (rank == 3) return "rare";
        return "common";
    }

    public record GachaDrawHistoryResult(Page<GachaDrawLogDTO> page, Map<String, Long> rarityStats) {}
}
