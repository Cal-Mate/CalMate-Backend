package com.ateam.calmate.exerciseRecords.query.mapper;

import com.ateam.calmate.exerciseRecords.query.dto.ExerciseRecordResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ExerciseRecordQueryMapper {

    List<ExerciseRecordResponse> selectExerciseRecords(
            @Param("memberId") Long memberId,
            @Param("date") LocalDate date
    );

    ExerciseRecordResponse selectExerciseRecordById(@Param("exerciseId") Long exerciseId);
}
