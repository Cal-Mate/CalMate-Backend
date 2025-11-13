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

    @Column(name = "badge_count", nullable = false)
    private Integer badgeCount;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /** 세 가지 기록이 모두 완료됐는지 */
    public boolean isAllCompleted() {
        return nvl(exerciseStatus) == 1 && nvl(mealStatus) == 1 && nvl(diaryStatus) == 1;
    }

    /** 이미 뱃지를 준 날인지 */
    public boolean isBadgeGiven() {
        return nvl(badgeYn) == 1;
    }

    /** 하루 뱃지 지급 처리 (엔티티 내부 캡슐화) */
    public void giveDailyBadgeIfPossible() {
        if (isBadgeGiven()) return;           // 이미 지급됨
        if (!isAllCompleted()) return; // 조건 미충족

        // 현재 요구사항: 하루 최대 1개
        this.badgeYn = 1;
        this.badgeCount = 1;           // 처음 지급이니까 1로 세팅
    }

    private int nvl(Integer val) {
        return val == null ? 0 : val;
    }
}
