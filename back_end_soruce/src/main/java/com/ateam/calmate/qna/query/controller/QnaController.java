package com.ateam.calmate.qna.query.controller;

import com.ateam.calmate.qna.query.dto.QnaResponse;
import com.ateam.calmate.qna.query.mapper.QnaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QnaController {

    private final QnaMapper qnaMapper;

    /** 전체 목록 */
    @GetMapping
    public ResponseEntity<List<QnaResponse>> getQnaList(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        return ResponseEntity.ok(qnaMapper.selectQnaList(limit, offset));
    }

    /** 내 문의 목록 (테스트용, 토큰 X) */
    @GetMapping("/my")
    public ResponseEntity<List<QnaResponse>> getMyQnaList(
            @RequestParam Long memberId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        return ResponseEntity.ok(qnaMapper.selectMyQnaList(memberId, limit, offset));
    }

    /** 상세 (댓글 포함) */
    @GetMapping("/{qnaId}")
    public ResponseEntity<QnaResponse> getQnaDetail(@PathVariable Long qnaId) {
        return ResponseEntity.ok(qnaMapper.selectQnaDetail(qnaId));
    }
}
