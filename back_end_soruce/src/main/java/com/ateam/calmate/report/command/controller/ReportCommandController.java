package com.ateam.calmate.report.command.controller;

import com.ateam.calmate.report.command.dto.ReportCreateRequest;
import com.ateam.calmate.report.command.service.ReportCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ReportCommandController {

    private final ReportCommandService reportCommandService;

    @PostMapping("/reports")
    public ResponseEntity<Long> createReport(
            @RequestPart("request") ReportCreateRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        return ResponseEntity.ok(reportCommandService.createReport(request, files));
    }

    @PatchMapping("/reports/{id}/process")
    public ResponseEntity<Void> processReport(@PathVariable Long id) {
        reportCommandService.processReport(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<Void> deleteMyReport(
            @PathVariable Long id,
            @RequestParam Long reporterMemberId
    ) {
        reportCommandService.deleteMyReport(id, reporterMemberId);
        return ResponseEntity.noContent().build();
    }
}
