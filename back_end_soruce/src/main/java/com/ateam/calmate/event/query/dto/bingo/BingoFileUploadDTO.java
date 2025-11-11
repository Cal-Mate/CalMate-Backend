package com.ateam.calmate.event.query.dto.bingo;

import com.ateam.calmate.event.command.entity.bingo.BingoFileUploadEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BingoFileUploadDTO {
    private Integer uploadId;
    private String name;
    private String mimeType;
    private String reName;
    private String path;
    private LocalDateTime createdAt;
    private Long extendFilePathId;
    private String baseUrl;
    private String fullUrl;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private Integer bingoCellId;         // (필수) FK
        private Long extendFilePathId;
        private MultipartFile file;

        public BingoFileUploadEntity toEntity(String fileName, String reName, String path, String mimeType) {
            return BingoFileUploadEntity.builder()
                    .name(fileName)
                    .reName(reName)
                    .path(path)
                    .mimeType(mimeType)
                    .bingoCellId(this.bingoCellId)
                    .extendFilePathId(this.extendFilePathId)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Integer id;
        private String name;
        private String mimeType;
        private String reName;
        private String path;
        private LocalDateTime createdAt;
        private Integer bingoCellId;        // FK
        private Long extendFilePathId;

        public static Response from(BingoFileUploadEntity fileUpload) {
            return Response.builder()
                    .id(fileUpload.getId())
                    .name(fileUpload.getName())
                    .mimeType(fileUpload.getMimeType())
                    .reName(fileUpload.getReName())
                    .path(fileUpload.getPath())
                    .createdAt(fileUpload.getCreatedAt())
                    .bingoCellId(fileUpload.getBingoCellId())
                    .extendFilePathId(fileUpload.getExtendFilePathId())
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UploadResponse {
        private Integer id;
        private String reName;
        private String path;
        private String url;
        private LocalDateTime createdAt;
        private Integer bingoCellId;
        private Integer row;               // 해당 칸의 행
        private Integer col;               // 해당 칸의 열
    }
}