package com.ateam.calmate.dietManagement.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealFoodResponse {

    private Long foodId;
    private String name;
    private Integer gram;
    private BigDecimal kcal;
    private BigDecimal carbo;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal sodium;
}
