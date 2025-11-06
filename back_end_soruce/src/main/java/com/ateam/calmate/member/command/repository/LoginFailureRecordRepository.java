package com.ateam.calmate.member.command.repository;

import com.ateam.calmate.member.command.entity.LoginFailureRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginFailureRecordRepository extends JpaRepository<LoginFailureRecord,Long> {

}
