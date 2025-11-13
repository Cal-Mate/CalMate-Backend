// dto/ReportListItemDto.java
package com.ateam.calmate.report.query.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class ReportListItemDto {
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

    // (원하시면) 리스트에서도 첨부 파일까지 함께 보시려면 아래 유지
    private List<ReportFileDto> files; // 선택
}