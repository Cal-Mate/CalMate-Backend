package com.ateam.calmate.ai.query.mapper;

import com.ateam.calmate.ai.command.dto.GoalType;
import com.ateam.calmate.ai.query.dto.GoalQueryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.math.BigInteger;

@Mapper
public interface GoalQueryMapper {
    GoalQueryDTO findMemberGoal(BigInteger memberId);
}
