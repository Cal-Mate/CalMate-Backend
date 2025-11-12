package com.ateam.calmate.member.query.dto;

import com.ateam.calmate.member.command.entity.Point;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 단건 포인트 내역 응답.
 */
public record PointHistoryResponse(
        Long id,
        Point.Distinction type,
        int points,
        String title,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime occurredAt
) {
}
