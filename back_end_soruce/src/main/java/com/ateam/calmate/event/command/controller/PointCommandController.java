package com.ateam.calmate.event.command.controller;

import com.ateam.calmate.event.command.dto.point.*;
import com.ateam.calmate.event.command.entity.point.PointCommandResultEntity;
import com.ateam.calmate.event.command.entity.point.PointLogEntity;
import com.ateam.calmate.event.command.repository.point.PointLogRepository;
import com.ateam.calmate.event.command.service.point.InsufficientPointException;
import com.ateam.calmate.event.command.service.point.PointCommandService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
public class PointCommandController {

    private final PointCommandService pointCommandService;
    private final PointLogRepository pointLogRepository;

    /** 적립(EARN) */
    @PostMapping("/{memberId}/earn")
    public ResponseEntity<PointCommandResponse> earn(
            @PathVariable long memberId,
            @Valid @RequestBody EarnRequest req
    ) {
        PointCommandResultEntity r = pointCommandService.earn(
                memberId,
                req.getAmount(),
                req.getSourceDomain(),
                req.getSourceId(),
                req.getReason(),
                req.getIdempotencyKey()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PointCommandResponse(r.pointLogId(), r.applied(), r.newBalance()));
    }

    /** 사용/차감(USE) */
    @PostMapping("/{memberId}/use")
    public ResponseEntity<PointCommandResponse> use(
            @PathVariable long memberId,
            @Valid @RequestBody UseRequest req
    ) {
        PointCommandResultEntity r = pointCommandService.use(
                memberId,
                req.getCost(),
                req.getSourceDomain(),
                req.getSourceId(),
                req.getReason(),
                req.getIdempotencyKey()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PointCommandResponse(r.pointLogId(), r.applied(), r.newBalance()));
    }

    /** 만료(EXPIRE) - 보통 시스템/스케줄러가 호출 */
    @PostMapping("/{memberId}/expire")
    public ResponseEntity<PointCommandResponse> expire(
            @PathVariable long memberId,
            @Valid @RequestBody ExpireRequest req
    ) {
        PointCommandResultEntity r = pointCommandService.expire(
                memberId,
                req.getAmount(),
                req.getReason() != null ? req.getReason() : "포인트 만료"
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PointCommandResponse(r.pointLogId(), r.applied(), r.newBalance()));
    }

    /** 조정(ADJUST) - 운영/관리자 용도 */
    @PostMapping("/{memberId}/adjust")
    public ResponseEntity<PointCommandResponse> adjust(
            @PathVariable long memberId,
            @Valid @RequestBody AdjustRequest req
    ) {
        PointCommandResultEntity r = pointCommandService.adjust(
                memberId,
                req.getDelta(),
                req.getReason(),
                req.getIdempotencyKey()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PointCommandResponse(r.pointLogId(), r.applied(), r.newBalance()));
    }

    /**
     * 보정(Compensate): 특정 로그를 반대로 상쇄
     * - 실제 삭제 대신 "반대 delta"로 ADJUST 기록을 남김
     * - 멱등키를 전달하지 않으면 서버가 생성
     */
    @PostMapping("/{memberId}/logs/{pointLogId}/compensate")
    public ResponseEntity<PointCommandResponse> compensate(
            @PathVariable long memberId,
            @PathVariable long pointLogId,
            @RequestParam(required = false) @NotBlank String idempotencyKey,
            @RequestParam(required = false) String reason
    ) {
        PointLogEntity target = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new IllegalArgumentException("point_log_id가 존재하지 않습니다: " + pointLogId));

        if (!target.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("memberId와 point_log의 소유자가 일치하지 않습니다.");
        }

        String idem = (idempotencyKey != null && !idempotencyKey.isBlank())
                ? idempotencyKey
                : "COMP_" + pointLogId + "_" + memberId;

        String msg = (reason != null && !reason.isBlank())
                ? reason
                : "Compensate point_log_id=" + pointLogId;

        // 반대 부호로 조정
        PointCommandResultEntity r = pointCommandService.adjust(memberId, -target.getDelta(), msg, idem);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PointCommandResponse(r.pointLogId(), r.applied(), r.newBalance()));
    }

    /** 실제 삭제는 허용하지 않음(불변 원장) */
    @DeleteMapping("/logs/{pointLogId}")
    public ResponseEntity<Map<String, Object>> deleteNotAllowed(@PathVariable long pointLogId) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(Map.of(
                        "error", "METHOD_NOT_ALLOWED",
                        "message", "point_log는 불변입니다. compensate 엔드포인트를 사용하세요.",
                        "pointLogId", pointLogId
                ));
    }

    // ---------- 예외 처리 ----------
    @ExceptionHandler(InsufficientPointException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficient(InsufficientPointException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "INSUFFICIENT_BALANCE", "message", e.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, Object>> handleValidation(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "INVALID_REQUEST", "message", e.getMessage()));
    }
}