package com.ateam.calmate.member.query.controller;

import com.ateam.calmate.member.query.dto.PointHistoryResponse;
import com.ateam.calmate.member.query.dto.PointSummaryResponse;
import com.ateam.calmate.member.query.service.PointQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
public class PointQueryController {

    private final PointQueryService pointQueryService;

    @GetMapping("/member/{memberId}/summary")
    public PointSummaryResponse getSummary(@PathVariable Long memberId) {
        return pointQueryService.getMemberSummary(memberId);
    }

    @GetMapping("/member/{memberId}/history")
    public List<PointHistoryResponse> getHistory(
            @PathVariable Long memberId,
            @RequestParam(value = "limit", defaultValue = "20") int limit
    ) {
        return pointQueryService.getRecentHistory(memberId, limit);
    }
}
