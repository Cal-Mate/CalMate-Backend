package com.ateam.calmate.community.command.controller;

import com.ateam.calmate.community.command.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<?> toggleCommentLike(
            @PathVariable Integer commentId,
            @RequestParam Long memberId
    ) {
        long likeCount = commentLikeService.toggleCommentLike(commentId, memberId);

        return ResponseEntity.ok(Map.of(
                "commentId", commentId,
                "likes", likeCount
        ));
    }
}