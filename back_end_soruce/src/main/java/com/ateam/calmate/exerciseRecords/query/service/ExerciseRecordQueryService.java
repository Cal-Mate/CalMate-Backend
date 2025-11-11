package com.ateam.calmate.exerciseRecords.query.service;

import com.ateam.calmate.exerciseRecords.query.dto.ExerciseRecordResponse;

import java.time.LocalDate;
import java.util.List;

public interface ExerciseRecordQueryService {

    List<ExerciseRecordResponse> getExerciseRecords(Long memberId, LocalDate date);

    ExerciseRecordResponse getExerciseRecord(Long exerciseId);
}
