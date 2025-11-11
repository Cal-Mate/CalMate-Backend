package com.ateam.calmate.dietManagement.command.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealRequest {

    private LocalDate date;
    private String type;
    private Long memberId;
    private FoodRequest food;
    private List<Long> keepFileIds;
}
