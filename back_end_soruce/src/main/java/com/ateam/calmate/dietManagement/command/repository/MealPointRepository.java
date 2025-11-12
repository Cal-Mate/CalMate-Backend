package com.ateam.calmate.dietManagement.command.repository;

import com.ateam.calmate.dietManagement.command.entity.MealPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface MealPointRepository extends JpaRepository<MealPoint, Long> {

    boolean existsByMemberIdAndReasonAndDistinctionAndHistoryTimeBetween(
            Long memberId,
            String reason,
            MealPoint.Distinction distinction,
            LocalDateTime start,
            LocalDateTime end
    );
}
