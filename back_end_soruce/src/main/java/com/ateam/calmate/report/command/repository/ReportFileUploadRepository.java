package com.ateam.calmate.report.command.repository;

import com.ateam.calmate.report.command.entity.ReportFileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportFileUploadRepository extends JpaRepository<ReportFileUpload, Integer> {
}

