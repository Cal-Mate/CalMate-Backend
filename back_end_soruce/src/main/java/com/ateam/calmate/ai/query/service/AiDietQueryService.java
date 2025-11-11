package com.ateam.calmate.ai.query.service;

import com.ateam.calmate.ai.command.dto.GoalType;
import com.ateam.calmate.ai.query.dto.GoalQueryDTO;
import com.ateam.calmate.ai.query.mapper.GoalQueryMapper;
import com.ateam.calmate.ai.query.mapper.AllergyQueryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
@Slf4j
public class AiDietQueryService {
    private final GoalQueryMapper goalQueryMapper;
    private final AllergyQueryMapper allergyQueryMapper;

    @Autowired
    public AiDietQueryService(GoalQueryMapper goalQueryMapper,
                              AllergyQueryMapper allergyQueryMapper) {
        this.goalQueryMapper = goalQueryMapper;
        this.allergyQueryMapper = allergyQueryMapper;
    }

    public GoalQueryDTO getMemberGoalInfo(BigInteger memberId) {
        return goalQueryMapper.findMemberGoal(memberId);
    }

    public List<String> getAllergy(BigInteger memberId) {
        return allergyQueryMapper.findMemberAllergyNames(memberId);
    }
}
