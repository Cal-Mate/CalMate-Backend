package com.ateam.calmate.event.query.mapper;

import com.ateam.calmate.event.enums.Distinction;
import com.ateam.calmate.event.enums.SourceDomain;
import com.ateam.calmate.event.query.dto.point.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Point 관련 조회 전용 Mapper (MyBatis)
 * - 잔액 조회
 * - 포인트 로그 조회
 * - 멱등키 중복 조회
 */
@Mapper
public interface PointQueryRepository {

    /**
     * 특정 회원의 잔액 단건 조회
     * @param memberId 회원 ID
     * @return 잔액 DTO (없으면 null)
     */
    MemberBalanceDTO selectMemberBalance(@Param("memberId") long memberId);

    /**
     * 특정 회원의 포인트 로그 목록 조회 (필터/페이징 가능)
     * @param memberId 회원 ID
     * @param distinction 구분(EARN, USE 등) - null 허용
     * @param sourceDomain 출처 도메인 - null 허용
     * @param startAt 조회 시작 시각 - null 허용
     * @param endAt 조회 종료 시각 - null 허용
     * @param orderBy 정렬문자열 (ex: "pl.created_at DESC") - null 허용
     * @param limit 페이지 크기 - null 허용
     * @param offset 시작 offset - null 허용
     */
    List<PointLogViewDTO> selectPointLogsByMember(
            @Param("memberId") long memberId,
            @Param("distinction") Distinction distinction,
            @Param("sourceDomain") SourceDomain sourceDomain,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt,
            @Param("orderBy") String orderBy,
            @Param("limit") Integer limit,
            @Param("offset") Integer offset
    );

    /**
     * 포인트 로그 단건 조회 (PK 기준)
     * @param pointLogId 로그 PK
     */
    PointLogViewDTO selectPointLogById(@Param("pointLogId") long pointLogId);

    /**
     * 멱등키로 기존 로그 조회 (중복요청 방지용)
     * @param memberId 회원 ID
     * @param idempotencyKey 멱등키
     */
    IdempotentLogCheckDTO selectLogByIdempotencyKey(
            @Param("memberId") long memberId,
            @Param("idempotencyKey") String idempotencyKey
    );

    /**
     * 특정 회원의 최신 로그 N건 조회
     * @param memberId 회원 ID
     * @param limit N건 제한
     */
    List<PointLogViewDTO> selectLatestLogs(
            @Param("memberId") long memberId,
            @Param("limit") int limit
    );
}