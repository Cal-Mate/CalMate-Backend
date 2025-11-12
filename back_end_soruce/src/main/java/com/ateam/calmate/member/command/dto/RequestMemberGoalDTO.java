package com.ateam.calmate.member.command.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestMemberGoalDTO {
    private Long id;
    private String goalType;         // ENUM('LOSS', 'MAINTAIN', 'INCREASE') → 문자열로 전달
    private BigDecimal targetValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private Long memberId;
}
