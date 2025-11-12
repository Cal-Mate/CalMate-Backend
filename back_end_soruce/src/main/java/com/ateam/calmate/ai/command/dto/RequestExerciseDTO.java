package com.ateam.calmate.ai.command.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestExerciseDTO {
    private String gender;
    private BigInteger memberId;
    private BigDecimal height;
    private BigDecimal weight;
    private Integer bodyMetric;
}
