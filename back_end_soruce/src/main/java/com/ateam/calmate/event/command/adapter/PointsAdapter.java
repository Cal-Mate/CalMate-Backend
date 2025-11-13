package com.ateam.calmate.event.command.adapter;

import com.ateam.calmate.event.command.port.PointsPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PointsAdapter implements PointsPort {

    @Override
    public void addPoints(Long memberId, int amount, String reason) {
        // TODO: 실제 포인트 시스템과 연동
        // 현재는 로그만 남김
        log.info("포인트 지급 - 회원ID: {}, 금액: {}, 사유: {}", memberId, amount, reason);

        // 향후 구현 예시:
        // pointRepository.save(new PointHistory(memberId, amount, reason));
        // memberRepository.updatePoints(memberId, amount);
    }
}