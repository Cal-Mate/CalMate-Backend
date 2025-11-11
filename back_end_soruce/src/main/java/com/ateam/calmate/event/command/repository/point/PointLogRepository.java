package com.ateam.calmate.event.command.repository.point;

import com.ateam.calmate.event.command.entity.point.PointLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointLogRepository extends JpaRepository<PointLogEntity, Long> {

    /** 멱등키로 기존 로그 조회 (중복요청 식별) */
    Optional<PointLogEntity> findByMemberIdAndIdempotencyKey(Long memberId, String idempotencyKey);
}