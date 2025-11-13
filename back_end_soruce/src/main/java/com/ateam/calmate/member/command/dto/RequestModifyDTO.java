package com.ateam.calmate.member.command.dto;

import com.ateam.calmate.ai.command.dto.GoalType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestModifyDTO {

    private Long id;
    private String email;
    private String nickname;
    private String phone;
    private BigDecimal weight;
    private BigDecimal height;
    private int bodyMetric;
    private GoalType goalType;
    private LocalDate endDate;
    private String bmr;
}