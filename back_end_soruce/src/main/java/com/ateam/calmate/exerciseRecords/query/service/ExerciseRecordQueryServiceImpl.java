package com.ateam.calmate.exerciseRecords.query.service;

import com.ateam.calmate.exerciseRecords.query.dto.ExerciseRecordResponse;
import com.ateam.calmate.exerciseRecords.query.mapper.ExerciseRecordQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseRecordQueryServiceImpl implements ExerciseRecordQueryService {

    private final ExerciseRecordQueryMapper exerciseRecordQueryMapper;

    @Override
    public List<ExerciseRecordResponse> getExerciseRecords(Long memberId, LocalDate date) {
        return exerciseRecordQueryMapper.selectExerciseRecords(memberId, date);
    }

    @Override
    public ExerciseRecordResponse getExerciseRecord(Long exerciseId) {
        return exerciseRecordQueryMapper.selectExerciseRecordById(exerciseId);
    }
}
