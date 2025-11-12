package com.ateam.calmate.community.query.controller;

import com.ateam.calmate.community.query.dto.PostListResponseDTO;
import com.ateam.calmate.community.query.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<List<PostListResponseDTO>> getPostList() {
        return ResponseEntity.ok(postService.getPostList());
    }
}
