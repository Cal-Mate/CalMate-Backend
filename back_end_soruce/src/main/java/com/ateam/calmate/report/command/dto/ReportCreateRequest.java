package com.ateam.calmate.report.command.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportCreateRequest {

    // 신고 제목
    private String title;

    // 신고 내용
    private String contents;

    // 신고 당한 회원 번호 (report.member_id2)
    private Long reportedMemberId;

    // 신고한 회원 번호 (report.member_id)
    private Long reporterMemberId;

    // 신고 출처가 게시글이면 게시글 번호
    private Integer postId;        // nullable

    // 신고 출처가 댓글이면 댓글 번호
    private Integer commentId;     // nullable

    // 신고 사유 코드 (report_base.id → report.report_id)
    private Integer reportBaseId;
}
