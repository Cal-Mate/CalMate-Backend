package com.ateam.calmate.exerciseRecords.command.service;

import com.ateam.calmate.exerciseRecords.command.entity.ExercisePoint;
import com.ateam.calmate.exerciseRecords.command.repository.ExercisePointRepository;
import com.ateam.calmate.member.command.entity.Member;
import com.ateam.calmate.member.command.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ExercisePointService {

    private final ExercisePointRepository exercisePointRepository;
    private final MemberRepository memberRepository;

    // ✅ 운동 기록당 5포인트
    private static final int EXERCISE_POINT = 5;

    public void earnExercisePoint(Long memberId) {

        // ✅ 포인트 로그 저장 (날짜 상관없이 매번 쌓음)
        ExercisePoint pointLog = ExercisePoint.builder()
                .memberId(memberId)
                .amount(EXERCISE_POINT)
                .distinction(ExercisePoint.Distinction.EARN)
                .reason("EXERCISE")
                .historyTime(LocalDateTime.now())
                .build();
        exercisePointRepository.save(pointLog);

        // ✅ 누적 포인트 갱신
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + memberId));
        int current = member.getPoint() == null ? 0 : member.getPoint();
        member.setPoint(current + EXERCISE_POINT);
    }
}
