package com.ateam.calmate.report.command.repository;

import com.ateam.calmate.report.command.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
