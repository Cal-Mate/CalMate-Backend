package com.ateam.calmate.exerciseRecords.command.repository;

import com.ateam.calmate.exerciseRecords.command.entity.ExercisePoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ExercisePointRepository extends JpaRepository<ExercisePoint, Long> {

    boolean existsByMemberIdAndReasonAndDistinctionAndHistoryTimeBetween(
            Long memberId,
            String reason,
            ExercisePoint.Distinction distinction,
            LocalDateTime start,
            LocalDateTime end
    );
}
