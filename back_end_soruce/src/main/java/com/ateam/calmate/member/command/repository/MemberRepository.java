package com.ateam.calmate.member.command.repository;

import com.ateam.calmate.member.command.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

//    Member findByEmailOrNameAndBirth(String email, String memName, LocalDate birth);
    List<Member> findByEmailOrNameAndBirth(String email, String memName, LocalDate birth);
}
