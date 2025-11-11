package com.ateam.calmate.report.command.controller;

import com.ateam.calmate.report.command.dto.ReportCreateRequest;
import com.ateam.calmate.report.command.dto.ReportCreateResponse;
import com.ateam.calmate.report.command.service.ReportCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportCommandController {

    private final ReportCommandService reportCommandService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReportCreateResponse> createReport(
            @RequestPart("request") ReportCreateRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        Long id = reportCommandService.createReport(request, files);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ReportCreateResponse(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(
            @PathVariable("id") Long reportId,
            @RequestParam("memberId") Long reporterMemberId
    ) {
        reportCommandService.deleteMyReport(reportId, reporterMemberId);
        return ResponseEntity.noContent().build();
    }
}
