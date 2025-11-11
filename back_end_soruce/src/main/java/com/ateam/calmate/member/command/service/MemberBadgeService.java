package com.ateam.calmate.member.command.service;

import com.ateam.calmate.calendar.query.mapper.CalendarMapper;
import com.ateam.calmate.member.command.entity.Member;
import com.ateam.calmate.member.command.entity.MemberRank;
import com.ateam.calmate.member.command.repository.MemberRankRepository;
import com.ateam.calmate.member.command.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberBadgeService {

    private final CalendarMapper calendarMapper;
    private final MemberRepository memberRepository;
    private final MemberRankRepository memberRankRepository;

    @Transactional
    public void updateMemberLevel(Long memberId) {
        // 1. 누적 뱃지 수 조회
        int totalBadges = calendarMapper.selectTotalBadgeCountByMember(memberId);

        // 2. rank 기준(최소 뱃지 수) 오름차순 조회
        List<MemberRank> ranks = memberRankRepository.findAll(
                Sort.by(Sort.Direction.ASC, "badgeCount")
        );

        // 기본 레벨 (예: 스타터)
        long newLevel = 1L;

        // 3. totalBadges 이상인 최대 badge_count 가진 rank 선택
        for (MemberRank rank : ranks) {
            Integer need = rank.getBadgeCount();
            if (need != null && totalBadges >= need) {
                newLevel = rank.getId();   // 해당 등급 ID를 member.level로 사용
            }
        }

        // 4. 회원 조회 후 레벨 반영
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다. id=" + memberId));

        member.setLevel(newLevel);
    }
}
