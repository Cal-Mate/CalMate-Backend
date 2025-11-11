package com.ateam.calmate.event.query.service.point;

import com.ateam.calmate.event.enums.Distinction;
import com.ateam.calmate.event.enums.SourceDomain;
import com.ateam.calmate.event.query.dto.point.*;
import com.ateam.calmate.event.query.mapper.PointQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointQueryService {

    private final PointQueryRepository mapper;

    public MemberBalanceDTO getBalance(long memberId) {
        return mapper.selectMemberBalance(memberId);
    }

    public List<PointLogViewDTO> getLogsByMember(
            long memberId,
            Distinction distinction,
            SourceDomain sourceDomain,
            LocalDateTime startAt,
            LocalDateTime endAt,
            String orderBy,
            Integer limit,
            Integer offset
    ) {
        // 기본 정렬/페이징 디폴트 설정 (필요 시)
        String effectiveOrderBy = (orderBy == null || orderBy.isBlank())
                ? "pl.created_at DESC"
                : orderBy;

        return mapper.selectPointLogsByMember(
                memberId, distinction, sourceDomain, startAt, endAt,
                effectiveOrderBy, limit, offset
        );
    }

    public PointLogViewDTO getLogById(long pointLogId) {
        return mapper.selectPointLogById(pointLogId);
    }

    public IdempotentLogCheckDTO getByIdempotencyKey(long memberId, String idempotencyKey) {
        return mapper.selectLogByIdempotencyKey(memberId, idempotencyKey);
    }

    public List<PointLogViewDTO> getLatestLogs(long memberId, int limit) {
        return mapper.selectLatestLogs(memberId, limit);
    }
}