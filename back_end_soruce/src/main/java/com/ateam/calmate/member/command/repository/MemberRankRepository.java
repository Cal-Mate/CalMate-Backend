package com.ateam.calmate.member.command.repository;

import com.ateam.calmate.member.command.entity.MemberRank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRankRepository extends JpaRepository<MemberRank, Long> {
}
