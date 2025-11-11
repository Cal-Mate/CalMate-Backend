package com.ateam.calmate.community.query.controller;

import com.ateam.calmate.community.query.dto.PostDetailDTO;
import com.ateam.calmate.community.query.service.PostServiceDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class PostControllerDetail {
    private final PostServiceDetail postServiceDetail;

    @GetMapping("/post/{postId}")
    public PostDetailDTO getPostDetail(@PathVariable("postId") int id,
                                       @RequestParam Long memberId) {
        return postServiceDetail.getPostDetail(id, memberId);
    }
}
