package com.ateam.calmate.diary.command.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DiaryUpdateRequest {
    private Integer weight;
    private String mood;
    private String condition;
    private String memo;

    // 삭제할 파일 id 목록 (선택)
    private List<Integer> deleteFileIds;
}
