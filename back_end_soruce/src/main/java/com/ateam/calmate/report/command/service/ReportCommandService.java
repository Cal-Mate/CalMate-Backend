package com.ateam.calmate.report.command.service;

import com.ateam.calmate.report.command.dto.ReportCreateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReportCommandService {

    Long createReport(ReportCreateRequest request, List<MultipartFile> files);

    void deleteMyReport(Long reportId, Long reporterMemberId);
}
