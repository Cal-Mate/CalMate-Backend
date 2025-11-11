package com.ateam.calmate.member.command.repository;

import com.ateam.calmate.member.command.entity.ProfileOfMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileOfMember, Long> {
    ProfileOfMember findByCumId(Long id);
}
