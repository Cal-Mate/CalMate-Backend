package com.ateam.calmate.exerciseRecords.command.repository;

import com.ateam.calmate.exerciseRecords.command.entity.ExerciseExtendFilePath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseExtendFilePathRepository
        extends JpaRepository<ExerciseExtendFilePath, Long> {

    Optional<ExerciseExtendFilePath> findByUrlPath(String urlPath);
}
