package com.ateam.calmate.exerciseRecords.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseRecordResponse {

    private Long exerciseId;
    private LocalDate date;
    private String type;
    private String category;
    private Integer min;
    private Integer burnedKcal;
    private LocalDateTime createAt;
    private Long memberId;

    private List<ExerciseFileResponse> files;
}
