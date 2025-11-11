package com.ateam.calmate.event.query.controller;

import com.ateam.calmate.event.enums.Distinction;
import com.ateam.calmate.event.enums.SourceDomain;
import com.ateam.calmate.event.query.dto.point.*;
import com.ateam.calmate.event.query.service.point.PointQueryService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
public class PointQueryController {

    private final PointQueryService service;

    /** 잔액 단건 조회 */
    @GetMapping("/{memberId}/balance")
    public ResponseEntity<MemberBalanceDTO> getBalance(@PathVariable long memberId) {
        MemberBalanceDTO dto = service.getBalance(memberId);
        return ResponseEntity.ok(dto);
    }

    /** 로그 목록 조회 (필터/기간/페이징) */
    @GetMapping("/{memberId}/logs")
    public ResponseEntity<List<PointLogViewDTO>> getLogsByMember(
            @PathVariable long memberId,
            @RequestParam(required = false) Distinction distinction,
            @RequestParam(required = false) SourceDomain sourceDomain,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startAt,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endAt,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) @Min(1) @Max(200) Integer limit,
            @RequestParam(required = false) @Min(0) Integer offset
    ) {
        List<PointLogViewDTO> list = service.getLogsByMember(
                memberId, distinction, sourceDomain, startAt, endAt, orderBy, limit, offset
        );
        return ResponseEntity.ok(list);
    }

    /** 로그 단건 조회 (PK) */
    @GetMapping("/logs/{pointLogId}")
    public ResponseEntity<PointLogViewDTO> getLogById(@PathVariable long pointLogId) {
        PointLogViewDTO dto = service.getLogById(pointLogId);
        return ResponseEntity.ok(dto);
    }

    /** 멱등키로 기존 로그 조회 */
    @GetMapping("/{memberId}/logs/by-idem")
    public ResponseEntity<IdempotentLogCheckDTO> getByIdempotencyKey(
            @PathVariable long memberId,
            @RequestParam("key") @NotBlank String idempotencyKey
    ) {
        IdempotentLogCheckDTO dto = service.getByIdempotencyKey(memberId, idempotencyKey);
        return ResponseEntity.ok(dto);
    }

    /** 최근 N건 */
    @GetMapping("/{memberId}/logs/latest")
    public ResponseEntity<List<PointLogViewDTO>> getLatest(
            @PathVariable long memberId,
            @RequestParam(defaultValue = "20") @Min(1) @Max(200) int limit
    ) {
        List<PointLogViewDTO> list = service.getLatestLogs(memberId, limit);
        return ResponseEntity.ok(list);
    }
}