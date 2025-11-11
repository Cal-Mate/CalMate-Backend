package com.ateam.calmate.event.query.dto.point;

import lombok.Getter;
import java.time.LocalDateTime;

/** point_balance 행 조회용 (읽기 전용) */
@Getter
public class MemberBalanceDTO {
    private long memberId;            // point_balance.member_id
    private int balance;              // point_balance.balance
    private LocalDateTime updatedAt;  // point_balance.updated_at
}