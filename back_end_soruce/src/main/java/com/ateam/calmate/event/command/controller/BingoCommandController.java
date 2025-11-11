package com.ateam.calmate.event.command.controller;

import com.ateam.calmate.common.KstYearMonth;
import com.ateam.calmate.event.command.dto.bingo.CheckCellWithUploadCommand;
import com.ateam.calmate.event.command.dto.bingo.CheckCellWithUploadResult;
import com.ateam.calmate.event.command.service.bingo.BingoBoardFactoryService;
import com.ateam.calmate.event.command.service.bingo.BingoCommandServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/bingo")
@RequiredArgsConstructor
public class BingoCommandController {

    private final BingoCommandServiceImpl service;
    private final BingoBoardFactoryService boardFactoryService;

    @PostMapping(value = "/boards/{boardId}/cells/{cellId}/check",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CheckCellWithUploadResult> checkCellWithUpload(
            @PathVariable Integer boardId,
            @PathVariable Integer cellId,
            @RequestParam Long memberId,
            @RequestParam(required = false) Long extendFilePathId,
            @RequestPart("file") MultipartFile file
    ) throws Exception {

        // 현재 월 보드 자동 생성 및 반환
        var currentYm = KstYearMonth.now();
        var ensuredBoard = boardFactoryService.ensureMonthlyBoard(memberId, currentYm, 5, null);

        // 요청된 boardId가 현재 월 보드가 아니면 400 Bad Request 반환
        if (!ensuredBoard.getId().equals(boardId)) {
            return ResponseEntity.badRequest().build();
        }

        var cmd = CheckCellWithUploadCommand.builder()
                .memberId(memberId)
                .boardId(boardId)
                .cellId(cellId)
                .extendFilePathId(extendFilePathId)
                .originalFilename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .build();

        var result = service.checkCellWithUpload(cmd, file.getInputStream());
        return ResponseEntity.ok(result);
    }
}
