package com.ateam.calmate.community.query.mapper;

import com.ateam.calmate.community.query.dto.PostListResponseDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    List<PostListResponseDTO> selectPostList();
}