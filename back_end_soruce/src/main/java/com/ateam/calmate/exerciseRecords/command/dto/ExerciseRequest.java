package com.ateam.calmate.exerciseRecords.command.dto;

import com.ateam.calmate.exerciseRecords.command.entity.Exercise;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseRequest {

    private Long memberId;
    private LocalDate date;
    private String type;
    private String category;
    private Integer min;
    private Integer burnedKcal;

    private List<Long> keepFileIds;

    public Exercise toEntity() {
        return Exercise.builder()
                .memberId(memberId)
                .date(date)
                .type(type)
                .category(category)
                .min(min)
                .burnedKcal(burnedKcal)
                .build();
    }
}
