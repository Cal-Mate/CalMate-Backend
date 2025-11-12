package com.ateam.calmate.ai.query.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AiDietQueryMapper {
    List<Integer> findTodayDietIds(BigInteger memberId, LocalDateTime start, LocalDateTime end);
}
