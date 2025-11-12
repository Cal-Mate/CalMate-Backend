package com.ateam.calmate.event.query.dto.gacha;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GachaQuantityDTO {
    private Long id;       // fk == gacha_prize.id
    private Integer count; // 남은 수량
}