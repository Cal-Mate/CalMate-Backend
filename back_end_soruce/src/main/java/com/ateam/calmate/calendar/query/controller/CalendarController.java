package com.ateam.calmate.calendar.query.controller;

import com.ateam.calmate.calendar.query.dto.CalendarDTO;
import com.ateam.calmate.calendar.query.mapper.CalendarMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarMapper calendarMapper;

    /**
     * 기간 조회
     * - /api/calendar?startDate=2025-11-01&endDate=2025-11-30
     * - /api/calendar?memberId=3&startDate=2025-11-01&endDate=2025-11-30
     * memberId 없으면 전체 회원 기준으로 조회
     */
    @GetMapping
    public List<CalendarDTO> getCalendarRange(
            @RequestParam(required = false) Long memberId,
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr
    ) {
        LocalDate startDate = LocalDate.parse(startDateStr.trim());
        LocalDate endDate = LocalDate.parse(endDateStr.trim());

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        return calendarMapper.selectCalendarRange(memberId, start, end);
    }

    /**
     * 일별 조회
     * - /api/calendar/day?day=2025-11-10
     * - /api/calendar/day?memberId=3&day=2025-11-10
     * memberId 없으면 전체 회원(모두) 기준
     */
    @GetMapping("/day")
    public List<CalendarDTO> getCalendarByDay(
            @RequestParam(required = false) Long memberId,
            @RequestParam("day") String dayStr
    ) {
        String day = dayStr.trim();
        return calendarMapper.selectCalendarByDay(memberId, day);
    }
}
