package com.ateam.calmate.exerciseRecords.command.controller;

import com.ateam.calmate.exerciseRecords.command.dto.ExerciseRequest;
import com.ateam.calmate.exerciseRecords.command.service.ExerciseCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping // 굳이 prefix 없으면 비워두기
public class ExerciseCommandController {

    private final ExerciseCommandService exerciseCommandService;

    @PostMapping("/exercise-records")
    public ResponseEntity<Long> createExerciseRecord(
            @RequestPart("request") ExerciseRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        Long id = exerciseCommandService.createExercise(request, files);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/exercise-records/update/{id}")
    public ResponseEntity<Void> updateExerciseRecord(
            @PathVariable Long id,
            @RequestPart("request") ExerciseRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws IOException {
        exerciseCommandService.updateExercise(id, request, files);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/exercise-records/delete/{id}")
    public ResponseEntity<Void> deleteExerciseRecord(@PathVariable Long id) {
        exerciseCommandService.deleteExercise(id);
        return ResponseEntity.ok().build();
    }
}
