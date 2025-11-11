package com.ateam.calmate.qna.command.controller;

import com.ateam.calmate.qna.command.dto.QnaCommentCreateRequest;
import com.ateam.calmate.qna.command.dto.QnaCommentUpdateRequest;
import com.ateam.calmate.qna.command.dto.QnaCreateRequest;
import com.ateam.calmate.qna.command.dto.QnaUpdateRequest;
import com.ateam.calmate.qna.command.entity.Qna;
import com.ateam.calmate.qna.command.entity.QnaComment;
import com.ateam.calmate.qna.command.service.QnaCommandService;
import com.ateam.calmate.qna.query.dto.*;
import com.ateam.calmate.qna.query.mapper.QnaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QnaCommandController {

    private final QnaCommandService qnaCommandService; // JPA
    private final QnaMapper qnaMapper;                 // MyBatis 조회용

    /** QnA 등록 */
    @PostMapping
    public ResponseEntity<?> createQna(@RequestBody QnaCreateRequest request) {
        Qna qna = qnaCommandService.createQna(request);          // JPA 저장
        QnaResponse detail = qnaMapper.selectQnaDetail(qna.getId()); // MyBatis 조회
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "문의가 등록되었습니다.",
                "data", detail
        ));
    }

    /** QnA 수정 */
    @PatchMapping("/{qnaId}")
    public ResponseEntity<?> updateQna(
            @PathVariable Long qnaId,
            @RequestBody QnaUpdateRequest request
    ) {
        qnaCommandService.updateQna(qnaId, request);             // JPA 수정
        QnaResponse detail = qnaMapper.selectQnaDetail(qnaId);   // MyBatis 조회
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "문의가 수정되었습니다.",
                "data", detail
        ));
    }

    /** QnA 삭제 */
    @DeleteMapping("/{qnaId}")
    public ResponseEntity<?> deleteQna(@PathVariable Long qnaId) {
        qnaCommandService.deleteQna(qnaId);                      // JPA 삭제
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "문의가 삭제되었습니다.",
                "qnaId", qnaId
        ));
    }

    /** 댓글 등록 */
    @PostMapping("/{qnaId}/comments")
    public ResponseEntity<?> createQnaComment(
            @PathVariable Long qnaId,
            @RequestBody QnaCommentCreateRequest request
    ) {
        QnaComment saved = qnaCommandService.createComment(qnaId, request); // JPA 저장
        Long targetQnaId = saved.getQna().getId();
        QnaResponse detail = qnaMapper.selectQnaDetail(targetQnaId);        // MyBatis 조회

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "댓글이 등록되었습니다.",
                "data", detail
        ));
    }

    /** 댓글 수정 */
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<?> updateQnaComment(
            @PathVariable Long commentId,
            @RequestBody QnaCommentUpdateRequest request
    ) {
        QnaComment updated = qnaCommandService.updateComment(commentId, request); // JPA 수정
        Long qnaId = updated.getQna().getId();
        QnaResponse detail = qnaMapper.selectQnaDetail(qnaId);                    // MyBatis 조회

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "댓글이 수정되었습니다.",
                "data", detail
        ));
    }
    /** 댓글 삭제 */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteQnaComment(@PathVariable Long commentId) {

        // JPA에서 qnaId까지 처리
        Long qnaId = qnaCommandService.deleteComment(commentId);

        // 최신 QnA 조회 (댓글 반영)
        QnaResponse detail = qnaMapper.selectQnaDetail(qnaId);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "댓글이 삭제되었습니다.",
                "data", detail
        ));
    }
}
