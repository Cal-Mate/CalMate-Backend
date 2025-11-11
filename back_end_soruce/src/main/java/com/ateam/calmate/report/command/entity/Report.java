package com.ateam.calmate.report.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                        // 신고번호

    private String title;                   // 신고 제목

    private String contents;                // 신고 내용

    private Boolean yn;                     // 신고 여부(처리 여부)

    private LocalDateTime date;             // 신고일자

    @Column(name = "report_image_url")
    private String reportImageUrl;          // 대표 이미지 URL (선택)

    @Column(name = "member_id2")
    private Long memberId2;                 // 신고 당한 회원 번호

    @Column(name = "post_id")
    private Integer postId;                 // 신고 대상 게시글 번호 (nullable)

    @Column(name = "comment_id")
    private Integer commentId;              // 신고 대상 댓글 번호 (nullable)

    @Column(name = "admin_id")
    private Long adminId;                   // 처리한 관리자 ID (nullable)

    @Column(name = "report_id")
    private Integer reportBaseId;           // 신고 사유 코드 (report_base.id)

    @Column(name = "member_id")
    private Long memberId;                  // 신고한 회원 번호

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReportFileUpload> fileUploads = new ArrayList<>();

    public void addFile(ReportFileUpload upload) {
        fileUploads.add(upload);
        upload.setReport(this);
    }
}
