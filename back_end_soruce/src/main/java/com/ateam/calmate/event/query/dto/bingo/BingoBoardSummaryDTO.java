package com.ateam.calmate.event.query.dto.bingo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BingoBoardSummaryDTO {
    // = bb.id
    private Integer boardId;

    // = bb.title
    private String title;

    // = bb.size
    private Integer size;

    // = bb.start_date, bb.end_date
    private LocalDate startDate;
    private LocalDate endDate;

    // = bb.created_at
    private LocalDateTime createdAt;

    // = bb.member_id
    private Long memberId;

    // 진행 정보 (집계 컬럼)
    // = checked_count, total_cells, completed_line_count 는 쿼리에서 계산해서 alias로 내려주기
    private Integer checkedCount;         // 체크된 칸 수
    private Integer totalCells;           // 전체 칸 수 (size*size)
    private Integer completedLineCount;   // 완성한 줄 수 (가로/세로/대각선 포함)

    // = is_completed (CASE WHEN completed_line_count = (size*2+2) ... 처럼 쿼리에서 산출해도 되고, 또는 서비스에서 계산)
    private Boolean completed;

    // 편의 정보
    // ex) 진행률(%) = ROUND(checkedCount / totalCells * 100, 1)
    private Double progressPercent;
}