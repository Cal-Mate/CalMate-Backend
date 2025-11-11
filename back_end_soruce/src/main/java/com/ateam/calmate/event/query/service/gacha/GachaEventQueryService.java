package com.ateam.calmate.event.query.service.gacha;

import com.ateam.calmate.event.query.dto.gacha.*;
import com.ateam.calmate.event.query.mapper.GachaEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GachaEventQueryService {

    private final GachaEventRepository repo;

    public GachaEventQueryService(GachaEventRepository repo) {
        this.repo = repo;
    }

    /** 현재 활성 이벤트 */
    public Optional<GachaEventDTO> findActiveEvent() {
        return repo.findActiveGachaEvent();
    }

    /** 이벤트 단건 조회 */
    public Optional<GachaEventDTO> findEventById(Long gachaEventId) {
        Assert.notNull(gachaEventId, "gachaEventId must not be null");
        return repo.findEventById(gachaEventId);
    }

    /** 이벤트의 톱랭크(최상위) 경품 */
    public Optional<GachaPrizeDTO> findTopRankPrizeByEvent(Long gachaEventId) {
        Assert.notNull(gachaEventId, "gachaEventId must not be null");
        return repo.findTopRankPrizeByEvent(gachaEventId);
    }

    /** 현재 보드 버전 */
    public Optional<Integer> findCurrentBoardVersion(Long gachaEventId) {
        Assert.notNull(gachaEventId, "gachaEventId must not be null");
        return repo.findCurrentBoardVersion(gachaEventId);
    }

    /** 특정 버전의 10x10 셀 목록 */
    public List<GachaSharedBoardDTO> findAllCellsByBoardVersion(Long gachaEventId, Integer boardVersion) {
        Assert.notNull(gachaEventId, "gachaEventId must not be null");
        Assert.notNull(boardVersion, "boardVersion must not be null");
        return repo.findAllCellsByBoardVersion(gachaEventId, boardVersion);
    }

    /** 보드 통계(열린 셀/전체 셀) */
    public Map<String, Integer> findBoardStats(Long gachaEventId, Integer boardVersion) {
        Assert.notNull(gachaEventId, "gachaEventId must not be null");
        Assert.notNull(boardVersion, "boardVersion must not be null");
        Integer opened = repo.countOpenedCells(gachaEventId, boardVersion);
        Integer total = repo.countTotalCells(gachaEventId, boardVersion);
        Map<String, Integer> stats = new HashMap<>();
        stats.put("opened", opened == null ? 0 : opened);
        stats.put("total", total == null ? 0 : total);
        return stats;
    }

    /** 경품 수량 */
    public Optional<GachaQuantityDTO> findQuantityByPrizeId(Long prizeId) {
        Assert.notNull(prizeId, "prizeId must not be null");
        return repo.findQuantityByPrizeId(prizeId);
    }

    /** 이벤트 리셋 정책 */
    public Optional<GachaResetDTO> findResetPolicyByEvent(Long gachaEventId) {
        Assert.notNull(gachaEventId, "gachaEventId must not be null");
        return repo.findResetPolicyByEvent(gachaEventId);
    }

    /** 이벤트의 모든 경품(랭크 오름차순) */
    public List<GachaPrizeDTO> findAllPrizesByEventOrderByRank(Long gachaEventId) {
        Assert.notNull(gachaEventId, "gachaEventId must not be null");
        return repo.findAllPrizesByEventOrderByRank(gachaEventId);
    }

    /** 특정 경품이 최상위 랭크인지 (COUNT>0) */
    public boolean isTopRankPrize(Long gachaEventId, Long prizeId) {
        Assert.notNull(gachaEventId, "gachaEventId must not be null");
        Assert.notNull(prizeId, "prizeId must not be null");
        Integer cnt = repo.checkIfTopRankPrize(gachaEventId, prizeId);
        return cnt != null && cnt > 0;
    }

    /** 리셋 정책이 TOP_RANK이고, 해당 경품이 톱랭크인지 종합판단 */
    public boolean canResetOnTopHit(Long gachaEventId, Long prizeId) {
        Assert.notNull(gachaEventId, "gachaEventId must not be null");
        Assert.notNull(prizeId, "prizeId must not be null");
        Boolean ok = repo.isTopRankResetPolicy(gachaEventId, prizeId);
        return ok != null && ok;
    }

    /** 이전 버전 셀들(백업/모니터링용) */
    public List<GachaSharedBoardDTO> findPreviousBoardCells(Long gachaEventId, Integer boardVersion) {
        Assert.notNull(gachaEventId, "gachaEventId must not be null");
        Assert.notNull(boardVersion, "boardVersion must not be null");
        return repo.findPreviousBoardCells(gachaEventId, boardVersion);
    }

    /** 현재 보드 스냅샷(버전+셀+통계)를 한 번에 제공 */
    public Optional<BoardSnapshot> getBoardSnapshot(Long gachaEventId) {
        Assert.notNull(gachaEventId, "gachaEventId must not be null");
        Optional<Integer> vOpt = repo.findCurrentBoardVersion(gachaEventId);
        if (vOpt.isEmpty()) return Optional.empty();
        Integer version = vOpt.get();
        List<GachaSharedBoardDTO> cells = repo.findAllCellsByBoardVersion(gachaEventId, version);
        Map<String, Integer> stats = findBoardStats(gachaEventId, version);
        return Optional.of(new BoardSnapshot(version, cells, stats.get("opened"), stats.get("total")));
    }

    /** 컨트롤러 응답용 스냅샷 DTO */
    public record BoardSnapshot(
            Integer boardVersion,
            List<GachaSharedBoardDTO> cells,
            Integer openedCount,
            Integer totalCount
    ) {}
}
