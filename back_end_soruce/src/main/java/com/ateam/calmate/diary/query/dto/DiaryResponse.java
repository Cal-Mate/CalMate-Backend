package com.ateam.calmate.diary.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DiaryResponse {
    private Integer id;
    private LocalDate day;
    private Integer weight;
    private String mood;
    private String condition;
    private String memo;
    private Long memberId;

    private List<DiaryFileResponse> files;
}
