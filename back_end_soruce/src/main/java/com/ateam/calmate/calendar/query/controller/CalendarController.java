package com.ateam.calmate.calendar.query.controller;

import com.ateam.calmate.calendar.query.dto.CalendarDTO;
import com.ateam.calmate.calendar.query.mapper.CalendarMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarMapper calendarMapper;

    /**
     * 📅 기간별 캘린더 조회
     */
    @GetMapping
    public List<CalendarDTO> getCalendarRange(
            @RequestParam(required = false) Long memberId,
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr
    ) {
        if (memberId == null) {
            memberId = 1L; // TODO: JWT 완료 후 제거
        }

        LocalDate startDate = LocalDate.parse(startDateStr.trim());
        LocalDate endDate = LocalDate.parse(endDateStr.trim());

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        return calendarMapper.selectCalendarRangeByMember(memberId, start, end);
    }

    /**
     * 📅 특정 일자 조회
     */
    @GetMapping("/day")
    public CalendarDTO getCalendarByDay(
            @RequestParam(required = false) Long memberId,
            @RequestParam("day") String dayStr
    ) {
        if (memberId == null) {
            memberId = 1L;
        }
        String day = dayStr.trim();
        return calendarMapper.selectCalendarByDay(memberId, day);
    }

    /**
     * 🏅 기간별 뱃지 개수 합계 조회
     * 예시:
     *  - /api/calendar/badge-count?startDate=2025-11-01&endDate=2025-11-30
     *  - /api/calendar/badge-count?memberId=3&startDate=2025-11-01&endDate=2025-11-30
     */
    @GetMapping("/badge-count")
    public Map<String, Object> getBadgeCount(
            @RequestParam(required = false) Long memberId,
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr
    ) {
        if (memberId == null) {
            memberId = 1L; // TODO: JWT 후 토큰에서 추출
        }

        LocalDate startDate = LocalDate.parse(startDateStr.trim());
        LocalDate endDate = LocalDate.parse(endDateStr.trim());

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        int totalBadgeCount = calendarMapper.selectBadgeCountByRange(memberId, start, end);

        return Map.of(
                "memberId", memberId,
                "startDate", startDate.toString(),
                "endDate", endDate.toString(),
                "badgeCount", totalBadgeCount
        );
    }

    @GetMapping("/badge-count/monthly")
    public Map<String, Object> getMonthlyBadgeCount(
            @RequestParam(required = false) Long memberId,
            @RequestParam("year") int year,
            @RequestParam("month") int month
    ) {
        if (memberId == null) {
            memberId = 1L; // TODO: JWT 적용 후 토큰에서 추출
        }

        // 해당 달의 시작일, 종료일 계산
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        int totalBadgeCount = calendarMapper.selectBadgeCountByRange(memberId, start, end);

        return Map.of(
                "memberId", memberId,
                "year", year,
                "month", month,
                "startDate", startDate.toString(),
                "endDate", endDate.toString(),
                "badgeCount", totalBadgeCount
        );
    }

    /**
     * 🏅 전체(누적) 뱃지 개수 합계 조회
     * 예시:
     *  - /api/calendar/badge-count
     *  - /api/calendar/badge-count?memberId=3
     */
    @GetMapping("/badge-total-count")
    public Map<String, Object> getTotalBadgeCount(
            @RequestParam(required = false) Long memberId
    ) {
        if (memberId == null) {
            memberId = 1L; // TODO: JWT 후 토큰에서 추출
        }

        int totalBadgeCount = calendarMapper.selectTotalBadgeCountByMember(memberId);

        return Map.of(
                "memberId", memberId,
                "badgeCount", totalBadgeCount
        );
    }
}
