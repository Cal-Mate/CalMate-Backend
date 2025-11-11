package com.ateam.calmate.event.command.entity.point;

/**
 * @param pointLogId 생성(혹은 기존) 로그 PK
 * @param applied    적용된 delta (0은 멱등으로 스킵됐을 때)
 * @param newBalance 적용 후 잔액
 */
public record PointCommandResultEntity(long pointLogId, int applied, int newBalance) {
}