package com.ateam.calmate.dietManagement.command.repository;

import com.ateam.calmate.dietManagement.command.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}
