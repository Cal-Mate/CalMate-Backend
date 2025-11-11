package com.ateam.calmate.member.command.repository;

import com.ateam.calmate.member.command.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmailOrMemNameAndBirth(String email, String memName, LocalDate birth);
}
