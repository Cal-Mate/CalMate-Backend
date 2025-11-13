package com.ateam.calmate.report.command.repository;

import com.ateam.calmate.report.command.entity.ReportComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportCommentRepository extends JpaRepository<ReportComment, Integer> {
}
