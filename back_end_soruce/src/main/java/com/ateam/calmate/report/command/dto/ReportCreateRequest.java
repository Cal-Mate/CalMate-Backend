package com.ateam.calmate.report.command.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportCreateRequest {
    private String title;
    private String contents;
    private Long reportedMemberId;   // 피신고자
    private Long reporterMemberId;   // 신고자
    private Integer postId;          // nullable
    private Integer commentId;       // nullable
    private Long reportBaseId;       // report_base.id
}
