package com.ateam.calmate.diary.command.controller;

import com.ateam.calmate.diary.command.dto.DiaryCreateRequest;
import com.ateam.calmate.diary.command.dto.DiaryUpdateRequest;
import com.ateam.calmate.diary.command.entity.Diary;
import com.ateam.calmate.diary.command.service.DiaryCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
public class DiaryCommandController {

    private final DiaryCommandService diaryCommandService;

    /** 📌 일기 등록 (JSON + 파일들) */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createDiary(
            @RequestPart("diary") DiaryCreateRequest req,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        Diary saved = diaryCommandService.createDiary(req, files);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "일기가 등록되었습니다.",
                "data", saved
        ));
    }

    /** 📌 일기 수정 (JSON + 파일 교체 가능) */
    @PatchMapping(value = "/{diaryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateDiary(
            @PathVariable Integer diaryId,
            @RequestPart("diary") DiaryUpdateRequest req,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        Diary updated = diaryCommandService.updateDiary(diaryId, req, files);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "일기가 수정되었습니다.",
                "data", updated
        ));
    }

    /** 📌 일기 삭제 */
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<?> deleteDiary(@PathVariable Integer diaryId) {
        diaryCommandService.deleteDiary(diaryId);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "일기가 삭제되었습니다.",
                "diaryId", diaryId
        ));
    }
}
