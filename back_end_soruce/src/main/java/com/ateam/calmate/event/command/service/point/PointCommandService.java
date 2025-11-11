package com.ateam.calmate.event.command.service.point;

import com.ateam.calmate.event.command.entity.point.PointBalanceEntity;
import com.ateam.calmate.event.command.entity.point.PointCommandResultEntity;
import com.ateam.calmate.event.command.entity.point.PointLogEntity;
import com.ateam.calmate.event.command.repository.point.PointBalanceRepository;
import com.ateam.calmate.event.command.repository.point.PointLogRepository;
import com.ateam.calmate.event.enums.Distinction;
import com.ateam.calmate.event.enums.SourceDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointCommandService {

    private final PointLogRepository pointLogRepository;
    private final PointBalanceRepository pointBalanceRepository;

    /**
     * 포인트 적용(적립/사용/만료/조정) - 멱등/음수방지/원자성 보장
     * 1) 잔액 행 보장 (없으면 balance=0 생성)
     * 2) 멱등키가 있으면 기존 로그 조회 (있으면 잔액 변경 없이 바로 반환)
     * 3) 로그 INSERT (동시경합은 UNIQUE 제약으로 한 건만 성공)
     * 4) 조건부 잔액 갱신 (delta>=0 || balance+delta>=0)
     */
    @Transactional
    public PointCommandResultEntity apply(long memberId,
                                          int delta,
                                          Distinction distinction,
                                          SourceDomain sourceDomain,
                                          Long sourceId,
                                          String reason,
                                          String idempotencyKey) {

        validateDelta(distinction, delta);

        // 0) 잔액 행 보장
        pointBalanceRepository.ensureRow(memberId);

        // 1) 멱등: 기존 로그가 있으면 잔액 변경 없이 기존 결과 반환
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            Optional<PointLogEntity> already = pointLogRepository.findByMemberIdAndIdempotencyKey(memberId, idempotencyKey);
            if (already.isPresent()) {
                int newBal = currentBalance(memberId);
                return new PointCommandResultEntity(already.get().getId(), 0, newBal);
            }
        }

        // 2) 로그 INSERT (경합 시 Unique 제약에 걸리면 기존 로그를 찾아 반환)
        PointLogEntity saved;
        try {
            PointLogEntity pl = PointLogEntity.builder()
                    .memberId(memberId)
                    .delta(delta)
                    .distinction(distinction)
                    .sourceDomain(sourceDomain)
                    .sourceId(sourceId)
                    .reason(reason)
                    .idempotencyKey(idempotencyKey)
                    .build();

            saved = pointLogRepository.save(pl);
            // 여기까지는 아직 커밋 전이므로, 이후 실패 시 전체 롤백됨
        } catch (DataIntegrityViolationException dup) {
            // (member_id, idempotency_key) UNIQUE 충돌 → 선점된 로그 반환
            if (idempotencyKey != null && !idempotencyKey.isBlank()) {
                Optional<PointLogEntity> exist = pointLogRepository.findByMemberIdAndIdempotencyKey(memberId, idempotencyKey);
                if (exist.isPresent()) {
                    int newBal = currentBalance(memberId);
                    return new PointCommandResultEntity(exist.get().getId(), 0, newBal);
                }
            }
            throw dup; // 예외적 상황
        }

        // 3) 조건부 잔액 갱신 (원자적/음수 방지)
        int updated = pointBalanceRepository.updateBalanceWithDelta(memberId, delta);
        if (updated == 0) {
            // 잔액 부족 등 → 트랜잭션 롤백 (로그 INSERT도 되돌아감)
            throw new InsufficientPointException("포인트 잔액이 부족합니다.");
        }

        int newBalance = currentBalance(memberId);
        return new PointCommandResultEntity(saved.getId(), delta, newBalance);
    }

    /** 편의 메서드: 적립(EARN) */
    @Transactional
    public PointCommandResultEntity earn(long memberId, int amount,
                                   SourceDomain sourceDomain, Long sourceId,
                                   String reason, String idemKey) {
        return apply(memberId, +Math.abs(amount), Distinction.EARN, sourceDomain, sourceId, reason, idemKey);
    }

    /** 편의 메서드: 사용/차감(USE) */
    @Transactional
    public PointCommandResultEntity use(long memberId, int cost,
                                  SourceDomain sourceDomain, Long sourceId,
                                  String reason, String idemKey) {
        return apply(memberId, -Math.abs(cost), Distinction.USE, sourceDomain, sourceId, reason, idemKey);
    }

    /** 편의 메서드: 만료(EXPIRE) */
    @Transactional
    public PointCommandResultEntity expire(long memberId, int amount, String reason) {
        return apply(memberId, -Math.abs(amount), Distinction.EXPIRE, SourceDomain.SYSTEM, null, reason, "EXPIRE_"+memberId+"_"+System.nanoTime());
    }

    /** 편의 메서드: 조정(ADJUST, +/− 허용) */
    @Transactional
    public PointCommandResultEntity adjust(long memberId, int delta, String reason, String idemKey) {
        return apply(memberId, delta, Distinction.ADJUST, SourceDomain.SYSTEM, null, reason, idemKey);
    }

    // ---------- 내부 유틸 ----------

    private void validateDelta(Distinction distinction, int delta) {
        if (delta == 0) throw new IllegalArgumentException("delta must be non-zero");
        if (distinction == Distinction.EARN && delta < 0)
            throw new IllegalArgumentException("EARN must be positive delta");
        if ((distinction == Distinction.USE || distinction == Distinction.EXPIRE) && delta > 0)
            throw new IllegalArgumentException("USE/EXPIRE must be negative delta");
        // ADJUST는 +/− 모두 허용
    }

    private int currentBalance(long memberId) {
        return pointBalanceRepository.findById(memberId)
                .map(PointBalanceEntity::getBalance)
                .orElse(0);
    }
}