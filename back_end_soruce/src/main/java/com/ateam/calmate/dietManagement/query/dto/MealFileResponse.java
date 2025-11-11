package com.ateam.calmate.dietManagement.query.dto;

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
public class MealFileResponse {

    private Long fileId;
    private String fileName;
    private String fileUrl;
    private String thumbUrl;
    private Integer uploadOrder;
}
