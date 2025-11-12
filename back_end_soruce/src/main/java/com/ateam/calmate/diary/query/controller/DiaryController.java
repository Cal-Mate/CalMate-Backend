package com.ateam.calmate.diary.query.controller;

import com.ateam.calmate.diary.query.dto.DiaryResponse;
import com.ateam.calmate.diary.query.mapper.DiaryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryMapper diaryMapper;

    // 목록 조회 (memberId 없으면 전체)
    @GetMapping
    public List<DiaryResponse> getDiaries(
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Integer offset
    ) {
        int resolvedLimit = (limit != null) ? limit : 10;
        int resolvedOffset = (offset != null) ? offset : 0;
        return diaryMapper.selectDiaryList(memberId, resolvedLimit, resolvedOffset);
    }

    // 날짜별 조회 (memberId 유무에 따라 필터)
    @GetMapping("/day")
    public List<DiaryResponse> getDiaryByDay(
            @RequestParam(required = false) Long memberId,
            @RequestParam String day
    ) {
        LocalDate parsedDay = LocalDate.parse(day.trim());
        return diaryMapper.selectDiaryListByMemberAndDay(memberId, parsedDay.toString());
    }

    // 상세 조회
    @GetMapping("/{diaryId}")
    public ResponseEntity<?> getDiaryDetail(
            @PathVariable Integer diaryId,
            @RequestParam(required = false) Long memberId
    ) {
        DiaryResponse diary = diaryMapper.selectDiaryDetail(diaryId, memberId);
        if (diary == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "일기를 찾을 수 없습니다.", "diaryId", diaryId));
        }
        return ResponseEntity.ok(diary);
    }
}
