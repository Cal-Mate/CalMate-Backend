// service/ReportQueryService.java
package com.ateam.calmate.report.query.service;

import com.ateam.calmate.report.query.dto.*;

public interface ReportQueryService {
    PageResponse<ReportListItemDto> getReports(ReportSearchCond cond);
    ReportDetailDto getReportDetail(Long id);
}
