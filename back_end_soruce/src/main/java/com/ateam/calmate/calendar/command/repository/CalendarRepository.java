package com.ateam.calmate.calendar.command.repository;

import com.ateam.calmate.calendar.command.entity.CalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {

    /**
     * 특정 회원의 특정 날짜 캘린더 조회
     */
    Optional<CalendarEntity> findByMemberIdAndCalDay(Long memberId, LocalDate calDay);
}
