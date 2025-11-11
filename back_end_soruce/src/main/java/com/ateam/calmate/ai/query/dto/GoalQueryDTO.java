package com.ateam.calmate.ai.query.dto;

import com.ateam.calmate.ai.command.dto.GoalType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GoalQueryDTO {
    private GoalType goalType;
    private BigDecimal targetValue;
    private LocalDate startDate;
    private LocalDate endDate;
}
