package com.ateam.calmate.event.command.adapter;

import com.ateam.calmate.event.command.port.FileStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class LocalFileStorageAdapter implements FileStoragePort {

    @Value("${file.upload.base-path:C:/uploadFiles}")
    private String basePath;

    @Override
    public SavedFile save(String directory, String originalFilename, String contentType, InputStream in, long size) {
        try {
            // 저장할 디렉토리 경로 생성
            Path dirPath = Paths.get(basePath, directory);
            Files.createDirectories(dirPath);

            // 고유한 파일명 생성 (UUID + 타임스탬프)
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String extension = getFileExtension(originalFilename);
            String storedFileName = UUID.randomUUID().toString() + "_" + timestamp + extension;

            // 파일 저장
            Path filePath = dirPath.resolve(storedFileName);
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);

            // 상대 경로 반환 (basePath 제외)
            String relativePath = directory + "/" + storedFileName;

            return new SavedFile(relativePath, storedFileName);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}