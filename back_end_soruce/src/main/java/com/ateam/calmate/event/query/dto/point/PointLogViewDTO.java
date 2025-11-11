package com.ateam.calmate.event.query.dto.point;

import com.ateam.calmate.event.enums.Distinction;
import com.ateam.calmate.event.enums.SourceDomain;
import lombok.Getter;
import java.time.LocalDateTime;

/** point_log 행 조회용 (읽기 전용) */
@Getter
public class PointLogViewDTO {
    private long pointLogId;      // point_log.point_log_id
    private long memberId;        // point_log.member_id
    private int delta;            // point_log.delta
    private Distinction distinction;   // 'EARN','USE','EXPIRE','ADJUST'
    private SourceDomain sourceDomain; // 'MEMBER','DIARY','CALENDAR','BINGO','GACHA','WORKOUT','FOOD','SYSTEM'
    private Long sourceId;             // NULL 허용
    private String reason;
    private String idempotencyKey;
    private LocalDateTime createdAt;
}