package com.ateam.calmate.diary.command.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryFileRequest {
    private String mime;
    private String path;
    private String state;
    private String originalFile;
    private String rename;
    private Long extendFilePathId;
}
