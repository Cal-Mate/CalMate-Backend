package com.ateam.calmate.member.command.repository;

import com.ateam.calmate.member.command.entity.AuthorityList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<AuthorityList, Integer> {
}
