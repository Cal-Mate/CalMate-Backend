package com.ateam.calmate.calendar.command.service;

import com.ateam.calmate.calendar.command.dto.CalendarCreateRequest;
import com.ateam.calmate.calendar.command.dto.CalendarUpdateRequest;
import com.ateam.calmate.calendar.command.entity.CalendarEntity;
import com.ateam.calmate.calendar.command.repository.CalendarRepository;
import com.ateam.calmate.member.command.service.MemberBadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CalendarCommandService {

    private final CalendarRepository calendarRepository;
    private final MemberBadgeService memberBadgeService;

    public CalendarEntity create(CalendarCreateRequest req) {
        CalendarEntity entity = CalendarEntity.builder()
                .calDay(req.getCalDay().toLocalDate())  // ✅ LocalDateTime → LocalDate
                .exerciseStatus(nvl(req.getExerciseStatus()))
                .mealStatus(nvl(req.getMealStatus()))
                .diaryStatus(nvl(req.getDiaryStatus()))
                .badgeCount(0)  // 초기값 0
                .memberId(req.getMemberId())
                .build();

        applyBadgeRule(entity);

        CalendarEntity saved = calendarRepository.save(entity);

        memberBadgeService.updateMemberLevel(saved.getMemberId());

        return saved;
    }

    public CalendarEntity update(Long id, CalendarUpdateRequest req) {
        CalendarEntity entity = calendarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("캘린더를 찾을 수 없습니다. id=" + id));

        if (req.getCalDay() != null) {
            entity.setCalDay(req.getCalDay().toLocalDate());  // ✅ 타입 변환
        }
        if (req.getExerciseStatus() != null) {
            entity.setExerciseStatus(req.getExerciseStatus());
        }
        if (req.getMealStatus() != null) {
            entity.setMealStatus(req.getMealStatus());
        }
        if (req.getDiaryStatus() != null) {
            entity.setDiaryStatus(req.getDiaryStatus());
        }

        applyBadgeRule(entity);

        CalendarEntity updated = calendarRepository.save(entity);

        memberBadgeService.updateMemberLevel(updated.getMemberId());

        return updated;
    }

    public void delete(Long id) {
        CalendarEntity entity = calendarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("캘린더를 찾을 수 없습니다. id=" + id));

        Long memberId = entity.getMemberId();

        calendarRepository.delete(entity);

        memberBadgeService.updateMemberLevel(memberId);
    }

    /**
     * 운동/식단/일기 모두 1이면 badge_yn = 1 & badge_count = 1, 아니면 badge_yn = 0 & badge_count = 0
     */
    private void applyBadgeRule(CalendarEntity entity) {
        int ex = nvl(entity.getExerciseStatus());
        int meal = nvl(entity.getMealStatus());
        int diary = nvl(entity.getDiaryStatus());

        if (ex == 1 && meal == 1 && diary == 1) {
            // 세 가지 조건 모두 충족: 뱃지 지급 (하루 최대 1개)
            if (nvl(entity.getBadgeYn()) == 0) {
                // 아직 뱃지 안 받은 경우만 지급
                entity.setBadgeYn(1);
                entity.setBadgeCount(1);
            }
        } else {
            // 조건 미충족: 뱃지 미지급
            entity.setBadgeYn(0);
            entity.setBadgeCount(0);
        }
    }

    private int nvl(Integer val) {
        return val == null ? 0 : val;
    }
}
