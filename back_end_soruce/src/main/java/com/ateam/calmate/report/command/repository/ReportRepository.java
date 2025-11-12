package com.ateam.calmate.report.command.repository;

import com.ateam.calmate.report.command.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    long countByMemberId2AndReportIdAndYnTrue(Long memberId2, Integer reportId);

    boolean existsByMemberId2AndReportIdAndYnTrue(Long memberId2, Integer reportId);
}
