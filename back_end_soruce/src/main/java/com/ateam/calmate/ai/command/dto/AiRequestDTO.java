package com.ateam.calmate.ai.command.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AiRequestDTO {
    private String gender;
    private BigDecimal height;
    private BigDecimal weight;
    private GoalType goalType;
    private BigDecimal targetValue;
    private int bodyMetric;
    private List<String> allergyNames = new ArrayList<>();
    private LocalDate startDate;
    private LocalDate endDate;
}
