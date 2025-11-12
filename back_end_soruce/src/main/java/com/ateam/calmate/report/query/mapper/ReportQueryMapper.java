// mapper/ReportQueryMapper.java
package com.ateam.calmate.report.query.mapper;

import com.ateam.calmate.report.query.dto.ReportDetailDto;
import com.ateam.calmate.report.query.dto.ReportFileDto;
import com.ateam.calmate.report.query.dto.ReportListItemDto;
import com.ateam.calmate.report.query.dto.ReportSearchCond;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportQueryMapper {

    long countReports(@Param("cond") ReportSearchCond cond);

    List<ReportListItemDto> findReports(@Param("cond") ReportSearchCond cond,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    ReportDetailDto findReportDetail(@Param("id") Long id);

    List<ReportFileDto> findReportFiles(@Param("reportId") Long reportId);
}
