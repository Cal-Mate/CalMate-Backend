package com.ateam.calmate.member.command.service;

import com.ateam.calmate.member.command.dto.RequestMemberGoalDTO;
import com.ateam.calmate.member.command.entity.MemberGoal;
import com.ateam.calmate.member.command.repository.GoalRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class GoalServiceImpl implements GoalService{

    private final GoalRepository goalRepository;
    private final ModelMapper modelMapper;

    public GoalServiceImpl(GoalRepository goalRepository
    , ModelMapper modelMapper) {
        this.goalRepository = goalRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RequestMemberGoalDTO getMemberGoal(Long id) {

        MemberGoal goal = goalRepository.findByMemberId(id);
        RequestMemberGoalDTO requestDTO = modelMapper.map(goal , RequestMemberGoalDTO.class);


        return requestDTO;
    }
}
