package com.ateam.calmate.event.query.controller;

import com.ateam.calmate.common.KstYearMonth;
import com.ateam.calmate.event.command.service.bingo.BingoBoardFactoryService;
import com.ateam.calmate.event.query.dto.bingo.BingoBoardDTO;
import com.ateam.calmate.event.query.dto.bingo.BingoBoardSummaryDTO;
import com.ateam.calmate.event.query.service.bingo.BingoQueryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/bingo")
public class BingoQueryController {

    private final BingoQueryServiceImpl service;
    private final BingoBoardFactoryService boardFactoryService;

    public BingoQueryController(BingoQueryServiceImpl service, BingoBoardFactoryService boardFactoryService) {
        this.service = service;
        this.boardFactoryService = boardFactoryService;
    }

    /** 월별 보드 요약 조회: /api/bingo/{memberId}/boards?month=2025-11 */
    @GetMapping("/{memberId}/boards")
    public ResponseEntity<BingoBoardSummaryDTO> getMonthlyBoardSummary(
            @PathVariable Long memberId,
            @RequestParam("month") String yearMonth
    ) {
        // 요청한 월의 YearMonth 파싱
        YearMonth requestedYm = YearMonth.parse(yearMonth);
        YearMonth currentYm = KstYearMonth.now();

        // 현재 월을 조회하는 경우 보드 자동 생성
        if (requestedYm.equals(currentYm)) {
            boardFactoryService.ensureMonthlyBoard(memberId, requestedYm, 5, null);
        }

        BingoBoardSummaryDTO dto = service.getMonthlyBoardSummary(memberId, yearMonth);
        return dto == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(dto);
    }

    /** 보드 상세 (기본 V2) */
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<BingoBoardDTO> getBoardDetail(@PathVariable Integer boardId) {
        BingoBoardDTO dto = service.getBoardDetail(boardId);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }

    /** 보드 상세 (V1 강제) */
    @GetMapping("/boards/{boardId}/v1")
    public ResponseEntity<BingoBoardDTO> getBoardDetailV1(@PathVariable Integer boardId) {
        BingoBoardDTO dto = service.getBoardDetailV1(boardId);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }
}