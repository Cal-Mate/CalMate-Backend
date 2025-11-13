package com.ateam.calmate.event.command.controller;

import com.ateam.calmate.event.command.adapter.InsufficientPointsException;
import com.ateam.calmate.event.command.port.PointsPort;
import com.ateam.calmate.member.command.entity.Point;
import com.ateam.calmate.member.command.repository.MemberRepository;
import com.ateam.calmate.member.command.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event/points")
public class EvnetPointsController {

    private final PointsPort pointsPort;
    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;

    /**
     * 포인트 적립 (이벤트 보상)
     */
    @PostMapping("/earn")
    @Transactional
    public ResponseEntity<Map<String, Object>> earnPoints(@RequestBody EarnPointsRequest request) {
        try {
            log.info("포인트 적립 요청 - 회원ID: {}, 금액: {}, 사유: {}",
                    request.memberId, request.amount, request.reason);

            // 회원 존재 확인
            if (!memberRepository.existsById(request.memberId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("회원을 찾을 수 없습니다."));
            }

            // 포인트 적립
            pointsPort.addPoints(request.memberId, request.amount, request.reason);

            // 포인트 히스토리 저장
            Point pointHistory = new Point();
            pointHistory.setMemberId(request.memberId);
            pointHistory.setPoint(request.amount);
            pointHistory.setDistinction(Point.Distinction.EARN);

            // 이벤트 종류에 따라 ID 설정
            if ("gacha".equalsIgnoreCase(request.eventType) && request.eventId != null) {
                pointHistory.setGachaEventId(request.eventId);
            } else if ("bingo".equalsIgnoreCase(request.eventType) && request.eventId != null) {
                pointHistory.setBingoBoardId(request.eventId.intValue());
            } else if ("diary".equalsIgnoreCase(request.eventType) && request.eventId != null) {
                pointHistory.setDiaryId(request.eventId.intValue());
            }

            pointRepository.save(pointHistory);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "포인트가 적립되었습니다.");
            response.put("amount", request.amount);
            response.put("memberId", request.memberId);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.error("포인트 적립 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("포인트 적립 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("포인트 적립 중 오류가 발생했습니다."));
        }
    }

    /**
     * 포인트 사용 (이벤트 참여)
     */
    @PostMapping("/use")
    @Transactional
    public ResponseEntity<Map<String, Object>> usePoints(@RequestBody UsePointsRequest request) {
        try {
            log.info("포인트 사용 요청 - 회원ID: {}, 금액: {}, 사유: {}",
                    request.memberId, request.amount, request.reason);

            // 회원 존재 확인
            if (!memberRepository.existsById(request.memberId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("회원을 찾을 수 없습니다."));
            }

            // 포인트 차감
            pointsPort.deductPoints(request.memberId, request.amount, request.reason);

            // 포인트 히스토리 저장
            Point pointHistory = new Point();
            pointHistory.setMemberId(request.memberId);
            pointHistory.setPoint(request.amount);
            pointHistory.setDistinction(Point.Distinction.USE);

            // 이벤트 종류에 따라 ID 설정
            if ("gacha".equalsIgnoreCase(request.eventType) && request.eventId != null) {
                pointHistory.setGachaEventId(request.eventId);
            } else if ("bingo".equalsIgnoreCase(request.eventType) && request.eventId != null) {
                pointHistory.setBingoBoardId(request.eventId.intValue());
            }

            pointRepository.save(pointHistory);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "포인트가 사용되었습니다.");
            response.put("amount", request.amount);
            response.put("memberId", request.memberId);

            return ResponseEntity.ok(response);

        } catch (InsufficientPointsException e) {
            log.error("포인트 부족: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            log.error("포인트 사용 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("포인트 사용 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("포인트 사용 중 오류가 발생했습니다."));
        }
    }

    /**
     * 회원의 현재 포인트 조회
     */
    @GetMapping("/balance/{memberId}")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable Long memberId) {
        try {
            var member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("memberId", memberId);
            response.put("balance", member.getPoint() != null ? member.getPoint() : 0);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("포인트 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("포인트 조회 중 오류가 발생했습니다."));
        }
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", message);
        return error;
    }

    // DTO classes
    public record EarnPointsRequest(
            Long memberId,
            int amount,
            String reason,
            String eventType,  // "gacha", "bingo", "diary"
            Long eventId
    ) {}

    public record UsePointsRequest(
            Long memberId,
            int amount,
            String reason,
            String eventType,  // "gacha", "bingo"
            Long eventId
    ) {}
}
