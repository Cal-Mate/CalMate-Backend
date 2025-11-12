package com.ateam.calmate.ai.command.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AiExerciseResponseDTO {
    private String planTitle;          // 예: "6주 감량용 복합 운동 플랜 (남 180cm/90kg → 목표 80kg, BMR 1800)"
    private String planTypeText;       // 예: "복합"
    private PlanDetail planDetail;     // totalTime / totalUseKcal / timesAWeek
    private List<String> planExerciseItem; // 예: ["스쿼트 12분", "푸시업 8분", ...]

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PlanDetail {
        private Integer totalTime;      // 52
        private Integer totalUseKcal;   // 890
        private Integer timesAWeek;     // 6
    }
}
