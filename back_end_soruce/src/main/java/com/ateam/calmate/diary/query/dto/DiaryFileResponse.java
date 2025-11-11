package com.ateam.calmate.diary.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DiaryFileResponse {
    private Integer id;
    private String mime;
    private String path;
    private LocalDate createdAt;
    private String state;
    private String originalFile;
    private String rename;        // alias로 매핑
    private Integer diaryId;
    private Long extendFilePathId;
}
