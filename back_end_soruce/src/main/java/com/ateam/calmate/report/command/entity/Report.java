package com.ateam.calmate.report.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contents;

    // tinyint(1) → boolean 매핑
    private Boolean yn;

    // 컬럼명이 date
    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "report_image_url", length = 500)
    private String reportImageUrl;

    // 피신고자
    @Column(name = "member_id2")
    private Long memberId2;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "admin_id")
    private Long adminId;

    // FK: report_base.id
    @Column(name = "report_id")
    private Integer reportId;

    // 신고자
    @Column(name = "member_id")
    private Long memberId;

    // 파일 업로드 연관
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReportFileUpload> fileUploads = new ArrayList<>();

    public void addFile(ReportFileUpload upload) {
        if (upload == null) return;
        fileUploads.add(upload);
        upload.setReport(this);
    }

    public void setYn(Boolean yn) { this.yn = yn; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    public void setReportImageUrl(String reportImageUrl) { this.reportImageUrl = reportImageUrl; }
}
