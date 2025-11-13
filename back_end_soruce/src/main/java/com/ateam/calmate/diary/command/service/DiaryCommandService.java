package com.ateam.calmate.diary.command.service;

import com.ateam.calmate.diary.command.dto.DiaryCreateRequest;
import com.ateam.calmate.diary.command.dto.DiaryUpdateRequest;
import com.ateam.calmate.diary.command.entity.Diary;
import com.ateam.calmate.diary.command.entity.DiaryFile;
import com.ateam.calmate.diary.command.repository.DiaryRepository;
import com.ateam.calmate.diary.command.repository.DiaryFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryCommandService {

    private final DiaryRepository diaryRepository;
    private final DiaryFileRepository diaryFileRepository;

    // 실제 파일 저장 디렉토리 (상대 경로)
    private static final String UPLOAD_DIR = "img/diary";

    // ✅ extend_file_path 테이블에서 "일기"용 경로 id로 교체하세요
    private static final long DIARY_EXTEND_FILE_PATH_ID = 3L;

    /* ====== 일기 등록 ====== */
    public Diary createDiary(DiaryCreateRequest req, List<MultipartFile> files) {
        Diary diary = Diary.builder()
                .day(req.getDay() != null ? req.getDay() : LocalDate.now())
                .weight(req.getWeight())
                .mood(req.getMood())
                .condition(req.getCondition())
                .memo(req.getMemo())
                .memberId(req.getMemberId())
                .build();

        diaryRepository.save(diary); // PK 확보

        if (files != null && !files.isEmpty()) {
            saveFiles(diary, files);
        }

        return diary;
    }

    /* ====== 일기 수정 ====== */
    public Diary updateDiary(Integer diaryId, DiaryUpdateRequest req, List<MultipartFile> files) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("일기를 찾을 수 없습니다. id=" + diaryId));

        if (req.getWeight() != null) diary.setWeight(req.getWeight());
        if (req.getMood() != null) diary.setMood(req.getMood());
        if (req.getCondition() != null) diary.setCondition(req.getCondition());
        if (req.getMemo() != null) diary.setMemo(req.getMemo());

        // 선택적으로 파일 삭제
        if (req.getDeleteFileIds() != null && !req.getDeleteFileIds().isEmpty()) {
            diaryFileRepository.deleteByIdInAndDiary_Id(req.getDeleteFileIds(), diaryId);
        }

        // 새 파일 추가
        if (files != null && !files.isEmpty()) {
            saveFiles(diary, files);
        }

        return diary;
    }

    /* ====== 일기 삭제 ====== */
    public void deleteDiary(Integer diaryId) {
        diaryRepository.deleteById(diaryId);
    }

    /* ====== 파일 저장 공통 ====== */
    private void saveFiles(Diary diary, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return;

        try {
            // back_end_soruce 디렉토리 안에 img/diary 경로 생성
            String baseDir = System.getProperty("user.dir");
            Path basePath = Paths.get(baseDir);

            // CalMate-Backend에서 실행되는 경우 back_end_soruce 하위로 이동
            if (!basePath.endsWith("back_end_soruce")) {
                basePath = basePath.resolve("back_end_soruce");
            }

            Path uploadPath = basePath.resolve(UPLOAD_DIR).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            System.out.println("=== Diary File Upload Debug ===");
            System.out.println("Base Directory: " + baseDir);
            System.out.println("Resolved Path: " + basePath.toString());
            System.out.println("Upload Path: " + uploadPath.toString());
            System.out.println("Upload Path exists: " + Files.exists(uploadPath));

            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                String originalName = file.getOriginalFilename();
                String ext = StringUtils.getFilenameExtension(originalName);
                String uuid = UUID.randomUUID().toString();
                String storedName = (ext != null && !ext.isBlank())
                        ? uuid + "." + ext
                        : uuid;

                Path target = uploadPath.resolve(storedName);
                file.transferTo(target.toFile());

                DiaryFile diaryFile = DiaryFile.builder()
                        .mime(file.getContentType())
                        .path("/" + UPLOAD_DIR + "/" + storedName)
                        .state("ACTIVE")
                        .originalFile(originalName)
                        .rename(storedName)
                        .extendFilePathId(3L) // ✅ 실제 FK 규칙에 맞는 값으로 넣어주세요
                        .build();

                diary.addFile(diaryFile);  // ✅ 여기!
            }

        } catch (Exception e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
