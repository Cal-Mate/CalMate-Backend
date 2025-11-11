package com.ateam.calmate.community.command.controller;

import com.ateam.calmate.community.command.dto.PostCreateRequestDTO;
import com.ateam.calmate.community.command.dto.PostUpdateRequestDTO;
import com.ateam.calmate.community.command.service.PostCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class PostCommandController {

    private final PostCommandService postCommandService;

    @PostMapping(value = "/post", consumes = "multipart/form-data")
    public ResponseEntity<?> createPost(@ModelAttribute PostCreateRequestDTO dto) {
        postCommandService.createPost(dto);
        return ResponseEntity.ok("게시글 등록 완료");
    }

    @PatchMapping(value = "/post/{postId}", consumes = "multipart/form-data")
    public ResponseEntity<?> updatePost(@PathVariable int postId, @ModelAttribute PostUpdateRequestDTO dto) {
        postCommandService.updatePost(postId, dto);
        return ResponseEntity.ok("게시글 수정 완료");
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable int postId) {
        postCommandService.deletePost(postId);
        return ResponseEntity.ok("게시글 삭제 완료");
    }
}