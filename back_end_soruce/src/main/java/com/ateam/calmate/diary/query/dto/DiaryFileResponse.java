package com.ateam.calmate.diary.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DiaryFileResponse {
    private Integer id;
    private String mime;
    private String path;
    private LocalDateTime createdAt;
    private String state;
    private String originalFile;
    private String rename;
    private Integer diaryId;
    private Long extendFilePathId;
}
