package com.ateam.calmate.community.query.controller;

import com.ateam.calmate.community.query.dto.CommentCreateRequestDTO;
import com.ateam.calmate.community.query.dto.CommentResponseDTO;
import com.ateam.calmate.community.query.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/post/{postId}/comments")
    public List<CommentResponseDTO> getComments(@PathVariable int postId,
                                                @RequestParam long memberId) {
        return commentService.getComments(postId, memberId);
    }

    @PostMapping("/post/{postId}/comments")
    public ResponseEntity<?> addComment(@PathVariable int postId,
                                        @RequestBody CommentCreateRequestDTO dto) {

        commentService.addComment(postId, dto.getMemberId(), dto.getContent(), dto.getParentId());

        String message = (dto.getParentId() == null)
                ? "댓글 작성이 완료되었습니다."
                : "대댓글 작성이 완료되었습니다.";

        Map<String, Object> response = new HashMap<>();
        response.put("memberId", dto.getMemberId());
        response.put("content", dto.getContent());
        response.put("parentId", dto.getParentId());
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

//    // ✅ 댓글 수정
//    @PatchMapping("/post/{postId}/comments/{commentId}")
//    public ResponseEntity<?> updateComment(@PathVariable int commentId,
//                                           @RequestBody Map<String, String> body) {
//        commentService.updateComment(commentId, body.get("content"));
//        return ResponseEntity.ok("댓글이 수정되었습니다.");
//    }

//    // ✅ 댓글 삭제
//    @DeleteMapping("/post/{postId}/comments/{commentId}")
//    public ResponseEntity<?> deleteComment(@PathVariable int commentId) {
//        commentService.deleteComment(commentId);
//        return ResponseEntity.ok("댓글이 삭제되었습니다.");
//    }

    // ✅ 댓글 수정 (작성자 검증 포함)
    @PatchMapping("/post/{postId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(
                                           @PathVariable int commentId,
                                           @RequestParam int memberId,
                                           @RequestBody Map<String, String> body) {

        commentService.updateComment(commentId, memberId, body.get("content"));
        return ResponseEntity.ok("댓글이 수정되었습니다.");
    }

    // ✅ 댓글 삭제 (작성자 검증 포함)
    @DeleteMapping("/post/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
                                           @PathVariable int commentId,
                                           @RequestParam int memberId) {

        commentService.deleteComment(commentId, memberId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}