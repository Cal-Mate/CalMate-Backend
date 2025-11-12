package com.ateam.calmate.dietManagement.command.service;

import com.ateam.calmate.dietManagement.command.entity.MealPoint;
import com.ateam.calmate.dietManagement.command.repository.MealPointRepository;
import com.ateam.calmate.member.command.entity.Member;
import com.ateam.calmate.member.command.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class MealPointService {

    private final MealPointRepository mealPointRepository;
    private final MemberRepository memberRepository;

    // ✅ 식단 기록당 5포인트
    private static final int MEAL_POINT = 5;

    public void earnMealPoint(Long memberId) {

        // ✅ 포인트 로그 저장 (날짜 상관없이 매번 쌓음)
        MealPoint pointLog = MealPoint.builder()
                .memberId(memberId)
                .amount(MEAL_POINT)
                .distinction(MealPoint.Distinction.EARN)
                .reason("MEAL")
                .historyTime(LocalDateTime.now())
                .build();
        mealPointRepository.save(pointLog);

        // ✅ 회원 누적 포인트 업데이트
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + memberId));
        int current = member.getPoint() == null ? 0 : member.getPoint();
        member.setPoint(current + MEAL_POINT);
    }
}
