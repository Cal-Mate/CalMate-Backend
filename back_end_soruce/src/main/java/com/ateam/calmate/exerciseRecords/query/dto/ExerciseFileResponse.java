package com.ateam.calmate.exerciseRecords.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseFileResponse {

    private Long fileId;
    private String name;
    private String url;
    private String thumbUrl;
    private Integer uploadOrder;
}