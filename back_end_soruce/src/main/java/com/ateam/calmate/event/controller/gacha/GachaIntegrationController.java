package com.ateam.calmate.event.controller.gacha;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gacha")
public class GachaIntegrationController {

    private final GachaEventQueryService eventQueryService;
    private final GachaBoardCommandService boardCommandService;

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
    public ResponseEntity<GachaBoardResponse> getBoardSnapshot(@PathVariable Long memberId,
                                                              @PathVariable Long eventId) {
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
        Page<GachaDrawLogDTO> history = boardCommandService.findMemberHistory(memberId, eventId, pageable);
        return new PageResponse<>(
                history.getContent(),
                history.getNumber() + 1,
                history.getSize(),
                history.getTotalElements(),
                history.getTotalPages(),
                history.isLast()
        );
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
            boolean last
    ) {}
}
