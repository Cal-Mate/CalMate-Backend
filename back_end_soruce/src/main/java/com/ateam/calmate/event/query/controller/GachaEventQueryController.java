package com.ateam.calmate.event.query.controller;

import com.ateam.calmate.event.query.dto.gacha.GachaPrizeDTO;
import com.ateam.calmate.event.query.dto.gacha.GachaSharedBoardDTO;
import com.ateam.calmate.event.query.service.gacha.GachaEventQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gacha/query")
public class GachaEventQueryController {

    private final GachaEventQueryService service;

    public GachaEventQueryController(GachaEventQueryService service) {
        this.service = service;
    }

    /** 1) 활성 이벤트 */
    @GetMapping("/active-event")
    public ResponseEntity<?> getActiveEvent() {
        return service.findActiveEvent()
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /** 2) 이벤트의 톱랭크 경품 */
    @GetMapping("/events/{eventId}/top-prize")
    public ResponseEntity<?> getTopPrize(@PathVariable Long eventId) {
        return service.findTopRankPrizeByEvent(eventId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /** 3) 현재 보드 버전 */
    @GetMapping("/events/{eventId}/board/current-version")
    public ResponseEntity<?> getCurrentBoardVersion(@PathVariable Long eventId) {
        return service.findCurrentBoardVersion(eventId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /** 4) 특정 보드 버전의 셀(10x10) */
    @GetMapping("/events/{eventId}/board/{version}/cells")
    public List<GachaSharedBoardDTO> getCells(
            @PathVariable Long eventId,
            @PathVariable Integer version
    ) {
        return service.findAllCellsByBoardVersion(eventId, version);
    }

    /** 5) 보드 통계(열린/전체) */
    @GetMapping("/events/{eventId}/board/{version}/stats")
    public ResponseEntity<?> getBoardStats(
            @PathVariable Long eventId,
            @PathVariable Integer version
    ) {
        return ResponseEntity.ok(service.findBoardStats(eventId, version));
    }

    /** 6) 경품 수량 */
    @GetMapping("/prizes/{prizeId}/quantity")
    public ResponseEntity<?> getPrizeQuantity(@PathVariable Long prizeId) {
        return service.findQuantityByPrizeId(prizeId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /** 7) 이벤트 리셋 정책 */
    @GetMapping("/events/{eventId}/reset-policy")
    public ResponseEntity<?> getResetPolicy(@PathVariable Long eventId) {
        return service.findResetPolicyByEvent(eventId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    /** 8) 이벤트의 모든 경품(랭크 오름차순) */
    @GetMapping("/events/{eventId}/prizes")
    public List<GachaPrizeDTO> getAllPrizes(@PathVariable Long eventId) {
        return service.findAllPrizesByEventOrderByRank(eventId);
    }

    /** 9) 특정 경품이 최상위 랭크인지 */
    @GetMapping("/events/{eventId}/is-top-rank")
    public ResponseEntity<?> isTopRank(
            @PathVariable Long eventId,
            @RequestParam Long prizeId
    ) {
        boolean yes = service.isTopRankPrize(eventId, prizeId);
        return ResponseEntity.ok(yes);
    }

    /** 10) 리셋 가능(TOP_RANK 정책 & 해당 경품이 최상위) */
    @GetMapping("/events/{eventId}/can-reset")
    public ResponseEntity<?> canReset(
            @PathVariable Long eventId,
            @RequestParam Long prizeId
    ) {
        boolean ok = service.canResetOnTopHit(eventId, prizeId);
        return ResponseEntity.ok(ok);
    }

    /** 11) 이전 버전 셀들(백업/모니터링용) */
    @GetMapping("/events/{eventId}/board/{version}/previous-cells")
    public List<GachaSharedBoardDTO> getPreviousCells(
            @PathVariable Long eventId,
            @PathVariable Integer version
    ) {
        return service.findPreviousBoardCells(eventId, version);
    }

    /** 12) 현재 보드 스냅샷(버전+셀+통계) */
    @GetMapping("/events/{eventId}/board/snapshot")
    public ResponseEntity<?> getBoardSnapshot(@PathVariable Long eventId) {
        return service.getBoardSnapshot(eventId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}