package com.ateam.calmate.exerciseRecords.command.service;

import com.ateam.calmate.exerciseRecords.command.dto.ExerciseRequest;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface ExerciseCommandService {
    Long createExercise(ExerciseRequest request, List<MultipartFile> files) throws IOException;
    void updateExercise(Long id, ExerciseRequest request, List<MultipartFile> files) throws IOException;
    void deleteExercise(Long id);
}
