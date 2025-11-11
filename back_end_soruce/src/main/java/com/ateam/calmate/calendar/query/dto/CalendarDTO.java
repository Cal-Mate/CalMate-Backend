package com.ateam.calmate.calendar.query.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CalendarDTO {
    private Long id;
    private LocalDateTime calDay;
    private Integer badgeCount;
    private Integer exerciseStatus;
    private Integer mealStatus;
    private Integer diaryStatus;
    private Long memberId;
}
