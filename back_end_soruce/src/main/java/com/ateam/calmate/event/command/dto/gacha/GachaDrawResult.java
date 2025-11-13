package com.ateam.calmate.event.command.dto.gacha;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GachaDrawResult {
    private boolean success;
    private String reason;       // "OK" / "SOLD_OUT" / "ERROR"
    private String prizeTier;    // "FIRST" ...
    private Long inventoryId;
    private Long eventId;
    private Long memberId;
    private long remainCount;    // 남은 재고 수
    private String type;         // "DRAW_RESULT"
    private Long cellId;         // 열린 셀 ID (gacha_shared_board.id)
    private Integer row;         // 셀 위치 (행)
    private Integer col;         // 셀 위치 (열)
}
