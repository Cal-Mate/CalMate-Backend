package com.ateam.calmate.report.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class ReportDetailDto {
    private Long id;
    private String title;
    private String contents;
    private Boolean yn;
    private LocalDateTime date;

    private Long reporterId;
    private String reporterName;

    private Long reportedId;
    private String reportedName;

    private Long postId;
    private Long commentId;

    private Long adminId;

    private Long reportBaseId;
    private String reportBaseTitle;
    private Integer requiredCount;
    private Integer banDays;

    private String reportImageUrl;

    private List<ReportFileDto> files;
}
