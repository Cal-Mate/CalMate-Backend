package com.ateam.calmate.community.query.service;

import com.ateam.calmate.community.query.dto.PostListResponseDTO;
import com.ateam.calmate.community.query.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;

    public List<PostListResponseDTO> getPostList() {
        return postMapper.selectPostList();
    }
}