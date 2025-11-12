package com.ateam.calmate.community.query.controller;

import com.ateam.calmate.community.query.dto.CommunityRankDTO;
import com.ateam.calmate.community.query.service.PostServiceRank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class PostControllerRank {

    private final PostServiceRank postServiceRank;

    @GetMapping("/ranking")
    public List<CommunityRankDTO> getRank() {
        return postServiceRank.getCommunityRank();
    }
}