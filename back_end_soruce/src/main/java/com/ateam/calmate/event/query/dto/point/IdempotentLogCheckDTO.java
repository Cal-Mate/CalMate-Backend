package com.ateam.calmate.event.query.dto.point;

import lombok.Getter;
import java.time.LocalDateTime;

/** 멱등키로 기존 로그 존재 여부 확인용 경량 DTO */
@Getter
public class IdempotentLogCheckDTO {
    private Long pointLogId;       // null이면 없음
    private Long memberId;
    private String idempotencyKey;
    private LocalDateTime createdAt;
}