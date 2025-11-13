package com.ateam.calmate.calendar.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "calendar")
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
    private LocalDate calDay;

    @Column(name = "exercise_status", nullable = false)
    private Integer exerciseStatus;

    @Column(name = "meal_status", nullable = false)
    private Integer mealStatus;

    @Column(name = "diary_status", nullable = false)
    private Integer diaryStatus;

    @Column(name = "badge_yn", nullable = false)
    private Integer badgeYn;   // 0 or 1

    @Column(name = "member_id", nullable = false)
    private Long memberId;
}
