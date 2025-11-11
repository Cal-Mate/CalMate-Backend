package com.ateam.calmate.ai.command.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class AiResponseDTO {
    @JsonProperty("plan_details")
    private PlanDetailsDTO planDetails;

    @JsonProperty("summary")
    private SummaryDTO summary;

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class PlanDetailsDTO {
        @JsonProperty("breakfast")
        private MealDTO breakfast;

        @JsonProperty("lunch")
        private MealDTO lunch;

        @JsonProperty("dinner")
        private MealDTO dinner;

        @JsonProperty("snack")
        private MealDTO snack;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class MealDTO { // 'breakfast', 'lunch' 등이 이 구조를 가집니다.
        @JsonProperty("items")
        private List<ItemDTO> items;

        @JsonProperty("total_kcal")
        private BigDecimal totalKcal;

        @JsonProperty("total_protein_g")
        private BigDecimal totalProteinG;

        @JsonProperty("total_fat_g")
        private BigDecimal totalFatG;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class ItemDTO { // 'items' 리스트 안의 개별 메뉴
        @JsonProperty("menu_name")
        private String menuName;

        @JsonProperty("serving_g")
        private BigDecimal servingG; // g수도 소수점이 있을 수 있으니 BigDecimal

        @JsonProperty("kcal")
        private BigDecimal kcal;

        @JsonProperty("protein_g")
        private BigDecimal proteinG;

        @JsonProperty("fat_g")
        private BigDecimal fatG;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class SummaryDTO { // 'summary' 객체
        @JsonProperty("total_kcal")
        private BigDecimal totalKcal;

        @JsonProperty("total_protein_g")
        private BigDecimal totalProteinG;

        @JsonProperty("total_fat_g")
        private BigDecimal totalFatG;

        @JsonProperty("target_bmr")
        private BigDecimal targetBmr;

        @JsonProperty("weekly_weight_change_kg")
        private BigDecimal weeklyWeightChangeKg;
    }

}
