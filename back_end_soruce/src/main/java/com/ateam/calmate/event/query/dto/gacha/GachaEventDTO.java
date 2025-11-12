package com.ateam.calmate.event.query.dto.gacha;

import com.ateam.calmate.event.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GachaEventDTO {
    private Long id;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Integer point;                 // 1회 필요 포인트
    private EventStatus status;            // DRAFT / ACTIVE / ...
    private Integer currentBoardVersion;   // 현재 운영중인 보드 버전
    private Long gachaResetId;             // fk -> gacha_reset.id
    private LocalDateTime createdAt;
}