package com.ateam.calmate.member.query.dto;

/**
 * Summary response returned to the frontend for a member's points snapshot.
 *
 * @param memberId     대상 회원 ID
 * @param totalEarned  누적 적립 포인트
 * @param totalUsed    누적 사용 포인트
 * @param currentPoint 현재 보유 포인트 (member.point)
 */
public record PointSummaryResponse(
        Long memberId,
        int totalEarned,
        int totalUsed,
        int currentPoint
) {
}
