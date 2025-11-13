package com.ateam.calmate.member.query.service;

import com.ateam.calmate.member.command.entity.Point;
import com.ateam.calmate.member.command.repository.MemberRepository;
import com.ateam.calmate.member.command.repository.PointRepository;
import com.ateam.calmate.member.query.dto.PointAggregateDTO;
import com.ateam.calmate.member.query.dto.PointHistoryResponse;
import com.ateam.calmate.member.query.dto.PointSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointQueryService {

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;

    public PointSummaryResponse getMemberSummary(Long memberId) {
        var memberOpt = memberRepository.findById(memberId);

        List<PointAggregateDTO> aggregates = pointRepository.sumByMemberId(memberId);
        int earned = 0;
        int used = 0;

        for (PointAggregateDTO aggregate : aggregates) {
            if (aggregate.distinction() == Point.Distinction.EARN) {
                earned = aggregate.total().intValue();
            } else if (aggregate.distinction() == Point.Distinction.USE) {
                used = aggregate.total().intValue();
            }
        }

        int currentPoint = memberOpt
                .map(m -> Optional.ofNullable(m.getPoint()).orElse(0))
                .orElse(0);
        return new PointSummaryResponse(memberId, earned, used, currentPoint);
    }

    public List<PointHistoryResponse> getRecentHistory(Long memberId, int limit) {
        if (!memberRepository.existsById(memberId)) {
            return List.of();
        }

        int cappedLimit = Math.min(Math.max(limit, 1), 100);
        Pageable pageable = PageRequest.of(0, cappedLimit);

        Page<Point> historyPage = pointRepository.findByMemberIdOrderByPointIdDesc(memberId, pageable);

        return historyPage.getContent().stream()
                .map(this::toHistoryResponse)
                .toList();
    }

    private PointHistoryResponse toHistoryResponse(Point point) {
        return new PointHistoryResponse(
                point.getPointId(),
                point.getDistinction(),
                Optional.ofNullable(point.getPoint()).orElse(0),
                resolveTitle(point),
                resolveOccurredAt(point)
        );
    }

    private String resolveTitle(Point point) {
        if (point.getGachaEventId() != null) {
            return point.getDistinction() == Point.Distinction.EARN ? "가챠 보상" : "가챠 참여";
        }
        if (point.getBingoBoardId() != null) {
            return point.getDistinction() == Point.Distinction.EARN ? "빙고 보상" : "빙고 참여";
        }
        if (point.getDiaryId() != null) {
            return "일기 작성 보상";
        }
        if (point.getCalenderId() != null) {
            return "캘린더 포인트";
        }
        return point.getDistinction() == Point.Distinction.EARN ? "포인트 적립" : "포인트 사용";
    }

    private LocalDateTime resolveOccurredAt(Point point) {
        return point.getHistoryTime();
    }
}
