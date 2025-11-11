package com.ateam.calmate.calendar.query.mapper;

import com.ateam.calmate.calendar.query.dto.CalendarDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CalendarMapper {

    // 기간별 캘린더 조회
    List<CalendarDTO> selectCalendarRangeByMember(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // 특정 일자 조회
    CalendarDTO selectCalendarByDay(
            @Param("memberId") Long memberId,
            @Param("day") String day   // 'YYYY-MM-DD'
    );

    // ⭐ 기간별 뱃지 개수 합계 조회
    int selectBadgeCountByRange(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // ✅ 전체(누적) 배지 합계
    int selectTotalBadgeCountByMember(@Param("memberId") Long memberId);


}
