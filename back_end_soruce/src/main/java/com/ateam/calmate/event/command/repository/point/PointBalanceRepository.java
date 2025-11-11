package com.ateam.calmate.event.command.repository.point;

import com.ateam.calmate.event.command.entity.point.PointBalanceEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PointBalanceRepository extends JpaRepository<PointBalanceEntity, Long> {

    /** 비관적 잠금으로 읽기 (원할 때 사용) */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select pb from PointBalanceEntity pb where pb.memberId = :memberId")
    Optional<PointBalanceEntity> findForUpdate(@Param("memberId") Long memberId);

    /** 없으면 생성 (UPSERT: ON DUPLICATE KEY UPDATE) - 네이티브 */
    @Modifying
    @Transactional
    @Query(
            value = """
              INSERT INTO point_balance (member_id, balance)
              VALUES (:memberId, 0)
              ON DUPLICATE KEY UPDATE balance = balance
              """,
            nativeQuery = true
    )
    int ensureRow(@Param("memberId") Long memberId);

    /**
     * 조건부 잔액 변경(음수 방지) - 네이티브 원자 갱신
     *  - delta >= 0  이면 무조건 갱신
     *  - delta < 0  이면 balance + delta >= 0 일 때만 갱신
     *  반환값: 갱신된 행 수(0이면 잔액부족 등으로 실패)
     */
    @Modifying
    @Transactional
    @Query(
            value = """
              UPDATE point_balance
                 SET balance = balance + :delta
               WHERE member_id = :memberId
                 AND (:delta >= 0 OR balance + :delta >= 0)
              """,
            nativeQuery = true
    )
    int updateBalanceWithDelta(@Param("memberId") Long memberId,
                               @Param("delta") int delta);
}