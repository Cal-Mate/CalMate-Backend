package com.ateam.calmate.community.query.service;

import com.ateam.calmate.community.query.dto.CommunityRankDTO;
import com.ateam.calmate.community.query.mapper.PostMapperRank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceRank {

    private final PostMapperRank postMapperRank;

    public List<CommunityRankDTO> getCommunityRank() {
        return postMapperRank.findCommunityRank();
    }
}