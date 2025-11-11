package com.ateam.calmate.event.command.dto.point;

import lombok.Getter;

@Getter
public class PointCommandResponse {
    private final long pointLogId;
    private final int applied;     // 적용된 delta (멱등이면 0)
    private final int newBalance;  // 적용 후 잔액

    public PointCommandResponse(long pointLogId, int applied, int newBalance) {
        this.pointLogId = pointLogId;
        this.applied = applied;
        this.newBalance = newBalance;
    }
}