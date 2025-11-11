package com.ateam.calmate.dietManagement.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class MealRecordRow {

    private Long mealId;
    private LocalDate date;
    private String type;
    private LocalDateTime createdAt;
    private Long memberId;

    private Long foodId;
    private String foodName;
    private Integer gram;
    private BigDecimal kcal;
    private BigDecimal carbo;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal sodium;

    private Long fileId;
    private String fileName;
    private String fileUrl;
    private String thumbUrl;
    private Integer uploadOrder;
}
