package com.ateam.calmate.event.command.port;

public interface PointsPort {
    void addPoints(Long memberId, int amount, String reason);
}