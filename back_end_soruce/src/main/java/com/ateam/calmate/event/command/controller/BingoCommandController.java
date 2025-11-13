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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/bingo")
@RequiredArgsConstructor
@Slf4j
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
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws Exception {
        log.debug("checkCellWithUpload requested - memberId: {}, boardId: {}, cellId: {}, extendFilePathId: {}",
                memberId, boardId, cellId, extendFilePathId);

        // 현재 월 보드 자동 생성 및 반환
        var currentYm = KstYearMonth.now();
        var ensuredBoard = boardFactoryService.ensureMonthlyBoard(memberId, currentYm, 5, null);

        // 요청된 boardId가 현재 월 보드가 아니면 400 Bad Request 반환
        if (!ensuredBoard.getId().equals(boardId)) {
            log.warn("Rejected bingo check - ensured board {} != requested board {} (memberId={}, yearMonth={})",
                    ensuredBoard.getId(), boardId, memberId, currentYm);
            return ResponseEntity.badRequest().build();
        }

        // 파일이 제공되지 않은 경우 400 Bad Request 반환
        if (file == null || file.isEmpty()) {
            log.warn("Rejected bingo check - missing file (memberId={}, boardId={}, cellId={})",
                    memberId, boardId, cellId);
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
        log.debug("checkCellWithUpload succeeded - memberId: {}, boardId: {}, cellId: {}", memberId, boardId, cellId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<DeleteFileResponse> deleteUploadedFile(
            @PathVariable Integer fileId,
            @RequestParam Long memberId
    ) {
        try {
            boolean deleted = service.deleteUploadedFile(fileId, memberId);
            return ResponseEntity.ok(new DeleteFileResponse(true, deleted, "파일이 삭제되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new DeleteFileResponse(false, false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new DeleteFileResponse(false, false, "파일 삭제 중 오류가 발생했습니다."));
        }
    }

    @DeleteMapping("/boards/{boardId}/cells/{cellId}/check")
    public ResponseEntity<CancelCheckResponse> cancelCellCheck(
            @PathVariable Integer boardId,
            @PathVariable Integer cellId,
            @RequestParam Long memberId
    ) {
        try {
            boolean cancelled = service.cancelCellCheck(boardId, cellId, memberId);
            return ResponseEntity.ok(new CancelCheckResponse(true, cancelled, "셀 체크가 취소되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CancelCheckResponse(false, false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new CancelCheckResponse(false, false, "셀 체크 취소 중 오류가 발생했습니다."));
        }
    }

    public record DeleteFileResponse(boolean success, boolean fileDeleted, String message) {}
    public record CancelCheckResponse(boolean success, boolean cancelled, String message) {}
}
