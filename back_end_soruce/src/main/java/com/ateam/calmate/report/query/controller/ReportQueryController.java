package com.ateam.calmate.report.query.controller;

import com.ateam.calmate.report.query.dto.PageResponse;
import com.ateam.calmate.report.query.dto.ReportListItemDto;
import com.ateam.calmate.report.query.dto.ReportSearchCond;
import com.ateam.calmate.report.query.service.ReportQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportQueryController {

    private final ReportQueryService reportQueryService;

    @GetMapping("/all")
    public ResponseEntity<PageResponse<ReportListItemDto>> getAllReports() {
        ReportSearchCond cond = new ReportSearchCond();
        cond.setStatus(null);
        cond.setTypeKeyword(null);
        cond.setKeyword(null);
        cond.setPage(0);
        cond.setSize(Integer.MAX_VALUE);

        return ResponseEntity.ok(reportQueryService.getReports(cond));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReportDetail(@PathVariable Long id) {
        var dto = reportQueryService.getReportDetail(id);
        return dto == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(dto);
    }
}
