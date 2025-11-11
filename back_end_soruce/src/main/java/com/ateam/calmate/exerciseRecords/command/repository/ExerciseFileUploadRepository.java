package com.ateam.calmate.exerciseRecords.command.repository;

import com.ateam.calmate.exerciseRecords.command.entity.ExerciseFileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseFileUploadRepository extends JpaRepository<ExerciseFileUpload, Long> {

    List<ExerciseFileUpload> findByExerciseId(Long exerciseId);
}
