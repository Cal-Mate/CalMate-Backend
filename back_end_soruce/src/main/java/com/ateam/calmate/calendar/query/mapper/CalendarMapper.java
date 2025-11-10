package com.ateam.calmate.calendar.query.mapper;

import com.ateam.calmate.calendar.query.dto.CalendarDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CalendarMapper {

    // 기간 조회 (memberId 없으면 전체)
    List<CalendarDTO> selectCalendarRange(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // 일별 조회 (memberId 없으면 전체)
    List<CalendarDTO> selectCalendarByDay(
            @Param("memberId") Long memberId,
            @Param("day") String day   // 'YYYY-MM-DD'
    );
}
