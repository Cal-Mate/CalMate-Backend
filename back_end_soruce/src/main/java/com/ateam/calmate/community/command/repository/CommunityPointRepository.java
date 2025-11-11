package com.ateam.calmate.community.command.repository;

import com.ateam.calmate.community.command.entity.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPointRepository extends JpaRepository<CommunityMember, Long> {
}
