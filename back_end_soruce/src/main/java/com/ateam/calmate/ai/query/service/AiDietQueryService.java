package com.ateam.calmate.ai.query.service;

import com.ateam.calmate.ai.command.dto.GoalType;
import com.ateam.calmate.ai.query.dto.GoalQueryDTO;
import com.ateam.calmate.ai.query.mapper.AiDietQueryMapper;
import com.ateam.calmate.ai.query.mapper.GoalQueryMapper;
import com.ateam.calmate.ai.query.mapper.AllergyQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class AiDietQueryService {
    private final AiDietQueryMapper aiDietQueryMapper;
    private final GoalQueryMapper goalQueryMapper;
    private final AllergyQueryMapper allergyQueryMapper;

    @Autowired
    public AiDietQueryService(AiDietQueryMapper aiDietQueryMapper, GoalQueryMapper goalQueryMapper,
                              AllergyQueryMapper allergyQueryMapper) {
        this.aiDietQueryMapper = aiDietQueryMapper;
        this.goalQueryMapper = goalQueryMapper;
        this.allergyQueryMapper = allergyQueryMapper;
    }

    public GoalQueryDTO getMemberGoalInfo(BigInteger memberId) {
        return goalQueryMapper.findMemberGoal(memberId);
    }

    public List<String> getAllergy(BigInteger memberId) {
        return allergyQueryMapper.findMemberAllergyNames(memberId);
    }

    public List<Integer> findTodayDietIds(BigInteger memberId, LocalDateTime start, LocalDateTime end) {
        return aiDietQueryMapper.findTodayDietIds(memberId, start, end);
    }
}
