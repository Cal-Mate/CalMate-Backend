package com.ateam.calmate.community.command.repository;

import com.ateam.calmate.community.command.entity.CommunityPointLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityPointLogRepository extends JpaRepository<CommunityPointLog, Long> {
}
