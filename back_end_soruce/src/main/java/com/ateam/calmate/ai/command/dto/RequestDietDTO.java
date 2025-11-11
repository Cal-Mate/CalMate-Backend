package com.ateam.calmate.ai.command.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestDietDTO {
    private String gender;
    private BigInteger memberId;
    private BigDecimal height;
    private BigDecimal weight;
    private Integer bodyMetric;
}
