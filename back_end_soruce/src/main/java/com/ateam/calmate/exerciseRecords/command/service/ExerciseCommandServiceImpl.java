package com.ateam.calmate.exerciseRecords.command.service;

import com.ateam.calmate.exerciseRecords.command.dto.ExerciseRequest;
import com.ateam.calmate.exerciseRecords.command.entity.Exercise;
import com.ateam.calmate.exerciseRecords.command.entity.ExerciseExtendFilePath;
import com.ateam.calmate.exerciseRecords.command.entity.ExerciseFileUpload;
import com.ateam.calmate.exerciseRecords.command.repository.ExerciseExtendFilePathRepository;
import com.ateam.calmate.exerciseRecords.command.repository.ExerciseFileUploadRepository;
import com.ateam.calmate.exerciseRecords.command.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ExerciseCommandServiceImpl implements ExerciseCommandService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseFileUploadRepository fileRepository;
    private final ExerciseExtendFilePathRepository extendPathRepository;

    private final Path exerciseRootDir =
            Paths.get(System.getProperty("user.dir"), "img", "exercise");

    @Override
    public Long createExercise(ExerciseRequest request, List<MultipartFile> files) throws IOException {
        Exercise exercise = exerciseRepository.save(request.toEntity());

        if (files != null && !files.isEmpty()) {
            saveFiles(files, exercise);
        }

        return exercise.getId();
    }

    @Override
    public void updateExercise(Long id, ExerciseRequest request, List<MultipartFile> files) throws IOException {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 운동이 존재하지 않습니다."));

        exercise.setDate(request.getDate());
        exercise.setType(request.getType());
        exercise.setCategory(request.getCategory());
        exercise.setMin(request.getMin());
        exercise.setBurnedKcal(request.getBurnedKcal());

        List<ExerciseFileUpload> oldFiles = fileRepository.findByExerciseId(id);
        List<Long> keepIds = request.getKeepFileIds() != null
                ? request.getKeepFileIds()
                : Collections.emptyList();

        for (ExerciseFileUpload f : oldFiles) {
            if (!keepIds.contains(f.getId())) {
                deletePhysicalFile(f);
                fileRepository.delete(f);
            }
        }

        if (files != null && !files.isEmpty()) {
            saveFiles(files, exercise);
        }
    }

    @Override
    public void deleteExercise(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 운동이 존재하지 않습니다."));

        List<ExerciseFileUpload> oldFiles = fileRepository.findByExerciseId(id);
        for (ExerciseFileUpload f : oldFiles) {
            deletePhysicalFile(f);
        }

        exerciseRepository.delete(exercise);
    }

    private void saveFiles(List<MultipartFile> files, Exercise exercise) throws IOException {
        Files.createDirectories(exerciseRootDir);

        ExerciseExtendFilePath extend = extendPathRepository.findByUrlPath("/img/exercise/")
                .orElseGet(() -> extendPathRepository.save(
                        ExerciseExtendFilePath.builder()
                                .urlPath("/img/exercise/")
                                .build()
                ));

        int startOrder = fileRepository.findByExerciseId(exercise.getId()).size() + 1;

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if (file.isEmpty()) continue;

            String originalName = StringUtils.cleanPath(file.getOriginalFilename());
            String ext = StringUtils.getFilenameExtension(originalName);
            String reName = UUID.randomUUID() + (ext != null ? "." + ext : "");

            Path savePath = exerciseRootDir.resolve(reName);
            file.transferTo(savePath.toFile());

            ExerciseFileUpload upload = ExerciseFileUpload.builder()
                    .exercise(exercise)
                    .name(originalName)
                    .type(file.getContentType())
                    .reName(reName)
                    .path("")
                    .thumbPath("")
                    .uploadOrder(startOrder + i)
                    .createAt(LocalDateTime.now())
                    .extendFilePath(extend)
                    .build();

            exercise.addFile(upload);
            fileRepository.save(upload);
        }
    }

    private void deletePhysicalFile(ExerciseFileUpload f) {
        if (f.getReName() == null || f.getReName().isEmpty()) return;

        try {
            Path filePath = exerciseRootDir.resolve(f.getReName());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
