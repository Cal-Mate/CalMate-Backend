package com.ateam.calmate.member.command.repository;

import com.ateam.calmate.member.command.entity.MemberAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAuthorityRepository extends JpaRepository<MemberAuthority,Long> {
}
