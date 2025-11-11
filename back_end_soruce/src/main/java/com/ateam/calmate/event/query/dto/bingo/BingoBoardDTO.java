package com.ateam.calmate.event.query.dto.bingo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BingoBoardDTO {
    private Integer boardId;
    private String title;
    private Integer size;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private Long memberId;

    // 진행 집계
    private Integer checkedCount;
    private Integer totalCells;
    private Integer completedLineCount;
    private Boolean completed;
    private Double progressPercent;

    // 하위 셀들
    private List<BingoCellDTO> cells;
}
