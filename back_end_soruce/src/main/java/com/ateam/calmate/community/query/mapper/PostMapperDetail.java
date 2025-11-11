package com.ateam.calmate.community.query.mapper;

import com.ateam.calmate.community.query.dto.PostDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface PostMapperDetail {
    PostDetailDTO findPostDetail(@Param("postId") int postId,
                                 @Param("memberId") Long memberId);
}