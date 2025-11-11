package com.ateam.calmate.diary.command.controller;

import com.ateam.calmate.diary.command.dto.DiaryCreateRequest;
import com.ateam.calmate.diary.command.dto.DiaryUpdateRequest;
import com.ateam.calmate.diary.command.entity.Diary;
import com.ateam.calmate.diary.command.service.DiaryCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
public class DiaryCommandController {

    private final DiaryCommandService diaryCommandService;

    /** 등록 */
    @PostMapping
    public ResponseEntity<?> createDiary(@RequestBody DiaryCreateRequest req) {
        Diary saved = diaryCommandService.createDiary(req);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "일기가 등록되었습니다.",
                "data", saved
        ));
    }

    /** 수정 */
    @PatchMapping("/{diaryId}")
    public ResponseEntity<?> updateDiary(
            @PathVariable Integer diaryId,
            @RequestBody DiaryUpdateRequest req
    ) {
        Diary updated = diaryCommandService.updateDiary(diaryId, req);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "일기가 수정되었습니다.",
                "data", updated
        ));
    }

    /** 삭제 */
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
