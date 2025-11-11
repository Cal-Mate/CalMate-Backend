package com.ateam.calmate.dietManagement.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealRecordResponse {

    private Long mealId;
    private LocalDate date;
    private String type;
    private LocalDateTime createdAt;
    private Long memberId;

    private MealFoodResponse food;

    @Builder.Default
    private List<MealFileResponse> files = new ArrayList<>();
}
