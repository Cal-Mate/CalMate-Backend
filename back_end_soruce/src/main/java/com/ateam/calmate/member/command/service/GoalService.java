package com.ateam.calmate.member.command.service;

import com.ateam.calmate.member.command.dto.RequestMemberGoalDTO;

public interface GoalService {
    RequestMemberGoalDTO getMemberGoal(Long id);
}
