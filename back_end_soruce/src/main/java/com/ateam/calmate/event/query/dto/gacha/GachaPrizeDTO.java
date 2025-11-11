package com.ateam.calmate.event.query.dto.gacha;

import com.ateam.calmate.event.enums.PrizeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GachaPrizeDTO {
    private Long id;
    private String name;
    private Map<String, Object> payloadJson; // 지급 payload(JSON) - 포인트/쿠폰코드 등
    private PrizeType prizeType;             // POINT/COUPON/ITEM/NOTHING/ETC
    private Integer rank;                    // 작을수록 상위 (1=최고)
    private Long gachaEventId;               // fk -> gacha_event.id
    private Integer quantity;                // 남은 수량 (nullable)
    private LocalDateTime createdAt;
}
