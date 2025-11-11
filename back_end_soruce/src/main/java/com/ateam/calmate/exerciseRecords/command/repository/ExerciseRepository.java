package com.ateam.calmate.exerciseRecords.command.repository;

import com.ateam.calmate.exerciseRecords.command.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
