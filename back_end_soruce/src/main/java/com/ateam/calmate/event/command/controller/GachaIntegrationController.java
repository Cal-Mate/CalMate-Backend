package com.ateam.calmate.event.command.controller;

import com.ateam.calmate.event.command.service.gacha.GachaBoardCommandService;
import com.ateam.calmate.event.query.dto.gacha.GachaDrawLogDTO;
import com.ateam.calmate.event.query.dto.gacha.GachaEventDTO;
import com.ateam.calmate.event.query.dto.gacha.GachaPrizeDTO;
import com.ateam.calmate.event.query.dto.gacha.GachaSharedBoardDTO;
import com.ateam.calmate.event.query.service.gacha.GachaEventQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gacha")
public class GachaIntegrationController {

    private final GachaEventQueryService eventQueryService;
    private final GachaBoardCommandService boardCommandService;
    private final com.ateam.calmate.event.command.service.gacha.GachaCommandService gachaCommandService;

    @GetMapping("/event/active")
    public ResponseEntity<GachaEventDTO> getActiveEvent() {
        return eventQueryService.findActiveEvent()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<GachaEventDTO> getEventById(@PathVariable Long eventId) {
        return eventQueryService.findEventById(eventId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/event/{eventId}/prizes")
    public List<GachaPrizeDTO> getEventPrizes(@PathVariable Long eventId) {
        return eventQueryService.findAllPrizesByEventOrderByRank(eventId);
    }

    @GetMapping("/member/{memberId}/event/{eventId}/board")
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<GachaBoardResponse> getBoardSnapshot(@PathVariable Long memberId,
                                                              @PathVariable Long eventId) {
        // 초기 보드가 없으면 자동 생성
        boardCommandService.ensureInitialBoardExists(eventId);

        return eventQueryService.getBoardSnapshot(eventId)
                .map(snapshot -> ResponseEntity.ok(
                        new GachaBoardResponse(
                                eventId,
                                memberId,
                                snapshot.boardVersion(),
                                snapshot.openedCount(),
                                snapshot.totalCount(),
                                snapshot.cells()
                        )
                ))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/cells/{cellId}/open")
    public GachaSharedBoardDTO openBoardCell(@PathVariable Long cellId,
                                             @RequestParam Long memberId,
                                             @RequestParam Long prizeId) {
        return boardCommandService.openCell(cellId, memberId, prizeId);
    }

    @PostMapping("/draw-logs")
    public GachaDrawLogDTO recordDrawLog(@RequestParam Long memberId,
                                         @RequestParam(name = "boardCellId") Long boardCellId,
                                         @RequestParam Long prizeId) {
        return boardCommandService.recordDraw(memberId, boardCellId, prizeId);
    }

    @GetMapping("/member/{memberId}/draw-history")
    public PageResponse<GachaDrawLogDTO> getDrawHistory(@PathVariable Long memberId,
                                                        @RequestParam(required = false) Long eventId,
                                                        @RequestParam(defaultValue = "1") int page,
                                                        @RequestParam(defaultValue = "10") int size) {
        int pageIndex = Math.max(page - 1, 0);
        int pageSize = Math.min(Math.max(size, 1), 50);
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        GachaBoardCommandService.GachaDrawHistoryResult result = boardCommandService.findMemberHistory(memberId, eventId, pageable);
        Page<GachaDrawLogDTO> history = result.page();
        return new PageResponse<>(
                history.getContent(),
                history.getNumber() + 1,
                history.getSize(),
                history.getTotalElements(),
                history.getTotalPages(),
                history.isLast(),
                result.rarityStats()
        );
    }

    /**
     * 가챠 뽑기 (포인트 차감 포함)
     * POST /api/gacha/event/{eventId}/draw
     */
    @PostMapping("/event/{eventId}/draw")
    public ResponseEntity<com.ateam.calmate.event.command.dto.gacha.GachaDrawResult> drawGacha(
            @PathVariable Long eventId,
            @RequestParam Long memberId,
            @RequestParam Long cellId) {
        com.ateam.calmate.event.command.dto.gacha.GachaDrawResult result =
                gachaCommandService.draw(eventId, memberId, cellId);
        return ResponseEntity.ok(result);
    }

    public record GachaBoardResponse(
            Long eventId,
            Long memberId,
            Integer boardVersion,
            Integer openedCount,
            Integer totalCount,
            List<GachaSharedBoardDTO> cells
    ) {}

    public record PageResponse<T>(
            List<T> content,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean last,
            Map<String, Long> rarityStats
    ) {}
}
