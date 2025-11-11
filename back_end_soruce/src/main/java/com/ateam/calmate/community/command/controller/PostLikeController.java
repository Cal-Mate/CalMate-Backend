package com.ateam.calmate.community.command.controller;

import com.ateam.calmate.community.command.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/post/{postId}/like")
    public ResponseEntity<Map<String, Object>> togglePostLike(
            @PathVariable Integer postId,
            @RequestParam Long memberId
    ) {
        long likeCount = postLikeService.togglePostLike(postId, memberId);
        return ResponseEntity.ok(Map.of(
                "postId", postId,
                "likes", likeCount
        ));
    }
}