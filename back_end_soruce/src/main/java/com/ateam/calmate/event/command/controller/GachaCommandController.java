package com.ateam.calmate.event.command.controller;

import com.ateam.calmate.event.command.service.gacha.GachaEventCommandService;
import com.ateam.calmate.event.enums.EventStatus;
import com.ateam.calmate.event.enums.PrizeType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gacha/command/events")
public class GachaCommandController {

    private final GachaEventCommandService service;

    /* ========= Request DTOs ========= */

    @Data
    public static class CreateEventRequest {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime startAt;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime endAt;
        private Integer point;
        private EventStatus status;             // DRAFT/ACTIVE/PAUSED/ENDED
        private Long resetPolicyId;             // null 가능
        private Integer currentBoardVersion;    // null 가능
    }

    @Data
    public static class UpdateEventRequest {
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime startAt;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime endAt;
        private Integer point;
        private EventStatus status;
        private Integer currentBoardVersion;
        private Long resetPolicyId; // null이면 유지
    }

    @Data
    public static class ChangeStatusRequest {
        private EventStatus status;
    }

    @Data
    public static class SetCurrentBoardVersionRequest {
        private Integer version;
    }

    @Data
    public static class SetResetPolicyRequest {
        private Long resetPolicyId;
    }

    @Data
    public static class AddPrizeRequest {
        private String name;
        private String payloadJson; // JSON 문자열
        private PrizeType prizeType;
        private Integer rank;
        private Integer initialQuantity; // null 가능
    }

    @Data
    public static class UpdatePrizeRequest {
        private String name;
        private String payloadJson; // JSON 문자열
        private PrizeType prizeType;
        private Integer rank;
        private Integer quantity; // null이면 수량 변경 없음
    }

    /* ========= APIs ========= */

    // 1) 이벤트 생성
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody CreateEventRequest req) {
        Long id = service.createEvent(
                req.getStartAt(),
                req.getEndAt(),
                req.getPoint(),
                req.getStatus(),
                req.getResetPolicyId(),
                req.getCurrentBoardVersion()
        );
        return ResponseEntity.ok(id);
    }

    // 2) 이벤트 수정(부분 업데이트)
    @PutMapping("/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable Long eventId, @RequestBody UpdateEventRequest req) {
        service.updateEvent(
                eventId,
                req.getStartAt(),
                req.getEndAt(),
                req.getPoint(),
                req.getStatus(),
                req.getCurrentBoardVersion(),
                req.getResetPolicyId()
        );
        return ResponseEntity.noContent().build();
    }

    // 3) 상태 변경
    @PatchMapping("/{eventId}/status")
    public ResponseEntity<?> changeStatus(@PathVariable Long eventId, @RequestBody ChangeStatusRequest req) {
        service.changeStatus(eventId, req.getStatus());
        return ResponseEntity.noContent().build();
    }

    // 4) 현재 보드 버전 설정
    @PatchMapping("/{eventId}/current-version")
    public ResponseEntity<?> setCurrentBoardVersion(@PathVariable Long eventId,
                                                    @RequestBody SetCurrentBoardVersionRequest req) {
        service.setCurrentBoardVersion(eventId, req.getVersion());
        return ResponseEntity.noContent().build();
    }

    // 5) 리셋 정책 연결/변경
    @PatchMapping("/{eventId}/reset-policy")
    public ResponseEntity<?> setResetPolicy(@PathVariable Long eventId, @RequestBody SetResetPolicyRequest req) {
        service.setResetPolicy(eventId, req.getResetPolicyId());
        return ResponseEntity.noContent().build();
    }

    // 6) 이벤트 삭제
    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long eventId) {
        service.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    // 7) 경품 추가
    @PostMapping("/{eventId}/prizes")
    public ResponseEntity<?> addPrize(@PathVariable Long eventId, @RequestBody AddPrizeRequest req) {
        Long prizeId = service.addPrize(
                eventId,
                req.getName(),
                req.getPayloadJson(),
                req.getPrizeType(),
                req.getRank(),
                req.getInitialQuantity()
        );
        return ResponseEntity.ok(prizeId);
    }

    // 8) 경품 수정
    @PutMapping("/prizes/{prizeId}")
    public ResponseEntity<?> updatePrize(@PathVariable Long prizeId, @RequestBody UpdatePrizeRequest req) {
        service.updatePrize(
                prizeId,
                req.getName(),
                req.getPayloadJson(),
                req.getPrizeType(),
                req.getRank(),
                req.getQuantity()
        );
        return ResponseEntity.noContent().build();
    }

    // 9) 경품 삭제
    @DeleteMapping("/prizes/{prizeId}")
    public ResponseEntity<?> deletePrize(@PathVariable Long prizeId) {
        service.deletePrize(prizeId);
        return ResponseEntity.noContent().build();
    }
}