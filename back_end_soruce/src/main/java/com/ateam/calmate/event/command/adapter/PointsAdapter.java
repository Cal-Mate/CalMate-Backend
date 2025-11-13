package com.ateam.calmate.event.command.adapter;

import com.ateam.calmate.event.command.port.PointsPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointsAdapter implements PointsPort {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void addPoints(Long memberId, int amount, String reason) {
        log.info("포인트 지급 - 회원ID: {}, 금액: {}, 사유: {}", memberId, amount, reason);

        int updated = jdbcTemplate.update(
            "UPDATE member SET point = COALESCE(point, 0) + ? WHERE id = ?",
            amount, memberId
        );

        if (updated == 0) {
            log.error("포인트 지급 실패 - 회원을 찾을 수 없습니다. 회원ID: {}", memberId);
            throw new IllegalArgumentException("회원을 찾을 수 없습니다. ID: " + memberId);
        }

        log.info("포인트 지급 완료 - 회원ID: {}, 금액: {}", memberId, amount);
    }

    @Override
    @Transactional
    public void deductPoints(Long memberId, int amount, String reason) {
        log.info("포인트 차감 시도 - 회원ID: {}, 금액: {}, 사유: {}", memberId, amount, reason);

        // 포인트가 충분한 경우에만 차감
        int updated = jdbcTemplate.update(
            "UPDATE member SET point = point - ? WHERE id = ? AND COALESCE(point, 0) >= ?",
            amount, memberId, amount
        );

        if (updated == 0) {
            log.error("포인트 차감 실패 - 회원ID: {}, 필요 포인트: {}", memberId, amount);
            throw new InsufficientPointsException("포인트가 부족합니다. 회원ID: " + memberId + ", 필요 포인트: " + amount);
        }

        log.info("포인트 차감 완료 - 회원ID: {}, 금액: {}", memberId, amount);
    }
}