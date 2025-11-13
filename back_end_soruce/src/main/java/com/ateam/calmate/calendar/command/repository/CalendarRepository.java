package com.ateam.calmate.calendar.command.repository;

import com.ateam.calmate.calendar.command.entity.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {
}
