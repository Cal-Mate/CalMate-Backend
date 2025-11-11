package com.ateam.calmate.event.query.dto.gacha;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GachaBoardSeedDTO {
    private Long id;
    private Long gachaEventId;     // fk -> gacha_event.id
    private Long gachaPrizeId;     // fk -> gacha_prize.id
    private Integer countPerBoard; // 10x10=100 중 이 경품 칸 수
}