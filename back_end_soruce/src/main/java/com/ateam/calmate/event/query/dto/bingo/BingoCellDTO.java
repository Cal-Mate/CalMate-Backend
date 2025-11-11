package com.ateam.calmate.event.query.dto.bingo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BingoCellDTO {
    // = bc.id
    private Integer cellId;

    // = bc.row, bc.col
    private Integer row;
    private Integer col;

    // = bc.label
    private String label;

    // = bc.is_checked, bc.checked_at
    private Boolean checked;
    private LocalDateTime checkedAt;

    // 첨부들 (0..n)
    private List<BingoFileUploadDTO> uploads;
}

/*
-- MyBatis에서 nested resultMap 매핑을 염두 (board 하나 + cell들 + upload들)
-- 예) board 조회 -> collection(cells) -> collection(uploads)
-- 성능을 위해 N+1을 피하려면
--  (1) 다중 조인 후 resultMap 중첩 컬렉션 사용
--  (2) boardId로 cell들 한번에, cellIds로 업로드 한번에 → 앱단에서 groupBy
-- 둘 중 트래픽/패턴에 맞게 선택 권장
*/