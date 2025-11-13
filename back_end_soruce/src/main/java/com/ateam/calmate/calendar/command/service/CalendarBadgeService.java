package com.ateam.calmate.calendar.command.service;

import com.ateam.calmate.calendar.command.entity.CalendarEntity;
import com.ateam.calmate.calendar.command.repository.CalendarRepository;
import com.ateam.calmate.member.command.entity.Member;
import com.ateam.calmate.member.command.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CalendarBadgeService {

    private final CalendarRepository calendarRepository;
    private final MemberRepository memberRepository;

    // 하루 뱃지 획득당 10포인트
    private static final int BADGE_POINT = 10;

    /**
     * 하루 뱃지 지급 체크 & 지급(JPA)
     *
     * @return true : 이번 호출에서 새로 지급됨
     *         false: 이미 지급됐거나 조건 미충족, row 없음 등
     */
    @Transactional
    public boolean checkAndGiveDailyBadge(Long memberId, LocalDate calDay) {

        CalendarEntity calendar = calendarRepository
                .findByMemberIdAndCalDay(memberId, calDay)
                .orElse(null);

        if (calendar == null) {
            // 아직 그날 calendar row 자체가 없으면 아무것도 안 함
            return false;
        }

        boolean beforeBadgeYn = calendar.isBadgeGiven();

        // 엔티티 내부 로직으로 뱃지 지급 시도
        calendar.giveDailyBadgeIfPossible();

        // JPA가 변경감지로 UPDATE 수행 (명시 save() 해줘도 OK)
        calendarRepository.save(calendar);

        // 이전엔 안 줬는데, 지금은 준 상태로 바뀌었으면 true
        boolean badgeGiven = !beforeBadgeYn && calendar.isBadgeGiven();

        // 뱃지가 새로 지급되었으면 포인트도 지급
        if (badgeGiven) {
            giveBadgePoint(memberId);
        }

        return badgeGiven;
    }

    /**
     * 뱃지 획득 시 포인트 지급
     */
    private void giveBadgePoint(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + memberId));

        int currentPoint = member.getPoint() == null ? 0 : member.getPoint();
        member.setPoint(currentPoint + BADGE_POINT);
    }
}
