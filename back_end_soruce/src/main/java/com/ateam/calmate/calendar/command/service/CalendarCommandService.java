package com.ateam.calmate.calendar.command.service;

import com.ateam.calmate.calendar.command.dto.CalendarCreateRequest;
import com.ateam.calmate.calendar.command.dto.CalendarUpdateRequest;
import com.ateam.calmate.calendar.command.entity.CalendarEntity;
import com.ateam.calmate.calendar.command.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CalendarCommandService {

    private final CalendarRepository calendarRepository;

    public CalendarEntity create(CalendarCreateRequest req) {
        CalendarEntity entity = CalendarEntity.builder()
                .calDay(req.getCalDay())
                .badgeCount(req.getBadgeCount())
                .exerciseStatus(req.getExerciseStatus())
                .mealStatus(req.getMealStatus())
                .diaryStatus(req.getDiaryStatus())
                .memberId(req.getMemberId())
                .build();
        return calendarRepository.save(entity);
    }

    public CalendarEntity update(Long id, CalendarUpdateRequest req) {
        CalendarEntity entity = calendarRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("캘린더를 찾을 수 없습니다. id=" + id));

        if (req.getCalDay() != null) entity.setCalDay(req.getCalDay());
        if (req.getBadgeCount() != null) entity.setBadgeCount(req.getBadgeCount());
        if (req.getExerciseStatus() != null) entity.setExerciseStatus(req.getExerciseStatus());
        if (req.getMealStatus() != null) entity.setMealStatus(req.getMealStatus());
        if (req.getDiaryStatus() != null) entity.setDiaryStatus(req.getDiaryStatus());

        return entity;
    }

    public void delete(Long id) {
        calendarRepository.deleteById(id);
    }
}
