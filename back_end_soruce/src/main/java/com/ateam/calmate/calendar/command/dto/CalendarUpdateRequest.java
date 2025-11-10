package com.ateam.calmate.calendar.command.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CalendarUpdateRequest {
    private LocalDateTime calDay;   // 변경 허용할지에 따라 사용
    private Integer badgeCount;
    private Integer exerciseStatus;
    private Integer mealStatus;
    private Integer diaryStatus;
}
