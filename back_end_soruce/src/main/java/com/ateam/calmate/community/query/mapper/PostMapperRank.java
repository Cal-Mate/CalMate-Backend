package com.ateam.calmate.community.query.mapper;

import com.ateam.calmate.community.query.dto.CommunityRankDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapperRank {
    List<CommunityRankDTO> findCommunityRank();
}