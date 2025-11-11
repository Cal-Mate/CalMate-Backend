package com.ateam.calmate.calendar.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "calender") // 실제 테이블명에 맞게 (calendar면 변경)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cal_day", nullable = false)
    private LocalDateTime calDay;

    @Column(name = "badge_count")
    private Integer badgeCount;

    @Column(name = "exercise_status")
    private Integer exerciseStatus;

    @Column(name = "meal_status")
    private Integer mealStatus;

    @Column(name = "diary_status")
    private Integer diaryStatus;

    @Column(name = "member_id", nullable = false)
    private Long memberId;
}
