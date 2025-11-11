package com.ateam.calmate.diary.command.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DiaryCreateRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate day;

    private Integer weight;
    private String mood;
    private String condition;
    private String memo;
    private Long memberId;

    private List<DiaryFileRequest> files;
}
