package com.ateam.calmate.member.command.repository;

import com.ateam.calmate.member.command.entity.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<UploadFile, Long> {
    UploadFile findByMemberId(Long id);
}
