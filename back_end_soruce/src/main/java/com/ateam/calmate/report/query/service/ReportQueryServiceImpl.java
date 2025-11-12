// service/ReportQueryServiceImpl.java
package com.ateam.calmate.report.query.service;

import com.ateam.calmate.report.query.dto.*;
import com.ateam.calmate.report.query.mapper.ReportQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportQueryServiceImpl implements ReportQueryService {

    private final ReportQueryMapper mapper;

    @Override
    public PageResponse<ReportListItemDto> getReports(ReportSearchCond cond) {
        int offset = Math.max(cond.getPage(), 0) * Math.max(cond.getSize(), 1);
        int limit  = Math.max(cond.getSize(), 1);

        long total = mapper.countReports(cond);
        List<ReportListItemDto> items = total == 0 ? List.of()
                : mapper.findReports(cond, offset, limit);

        return PageResponse.<ReportListItemDto>builder()
                .total(total)
                .page(cond.getPage())
                .size(cond.getSize())
                .items(items)
                .build();
    }

    @Override
    public ReportDetailDto getReportDetail(Long id) {
        ReportDetailDto detail = mapper.findReportDetail(id);
        if (detail == null) return null;
        detail.setFiles(mapper.findReportFiles(id));
        return detail;
    }
}
