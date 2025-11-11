package com.ateam.calmate.exerciseRecords.query.controller;

import com.ateam.calmate.exerciseRecords.query.dto.ExerciseRecordResponse;
import com.ateam.calmate.exerciseRecords.query.service.ExerciseRecordQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exercise-records")
public class ExerciseRecordQueryController {

    private final ExerciseRecordQueryService exerciseRecordQueryService;

    @GetMapping
    public ResponseEntity<List<ExerciseRecordResponse>> getExerciseRecords(
            @RequestParam Long memberId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        List<ExerciseRecordResponse> records =
                exerciseRecordQueryService.getExerciseRecords(memberId, date);
        return ResponseEntity.ok(records);
    }

    // 🔹 운동번호 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseRecordResponse> getExerciseRecord(@PathVariable Long id) {
        ExerciseRecordResponse record = exerciseRecordQueryService.getExerciseRecord(id);
        if (record == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(record);
    }
}
