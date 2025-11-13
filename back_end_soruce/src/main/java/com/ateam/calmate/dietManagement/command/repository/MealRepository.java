package com.ateam.calmate.dietManagement.command.repository;

import com.ateam.calmate.dietManagement.command.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MealRepository extends JpaRepository<Meal, Long> {
    int countByMemberIdAndDate(Long memberId, LocalDate date);
}
