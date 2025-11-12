package com.ateam.calmate.exerciseRecords.command.repository;

import com.ateam.calmate.exerciseRecords.command.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    boolean existsByMemberIdAndDate(Long memberId, LocalDate date); // ✅ 추가
}
