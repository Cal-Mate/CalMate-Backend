package com.ateam.calmate.member.command.repository;

import com.ateam.calmate.member.command.entity.MemberGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<MemberGoal, Long> {
    MemberGoal findByMemberId(Long id);
}
