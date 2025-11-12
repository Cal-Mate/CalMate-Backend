package com.ateam.calmate.report.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ReportListItemDto {
    private Long id;
    private String title;
    private String reportBaseTitle;   // 신고 유형명 (욕설/도배 등)
    private Boolean yn;               // 처리 여부
    private LocalDateTime date;       // 생성일

    private Long reporterId;          // 신고자
    private String reporterName;

    private Long reportedId;          // 피신고자
    private String reportedName;

    private Long postId;              // 대상이 게시글이면 세팅
    private Long commentId;           // 대상이 댓글이면 세팅

    private String reportImageUrl;    // 리스트 썸네일용(스냅샷)
}
