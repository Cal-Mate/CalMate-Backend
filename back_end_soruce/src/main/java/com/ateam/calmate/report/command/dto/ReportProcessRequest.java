package com.ateam.calmate.report.command.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportProcessRequest {
    private Long adminId;  // 신고를 확정하는 관리자 ID
}
