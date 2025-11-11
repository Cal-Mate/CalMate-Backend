package com.ateam.calmate.report.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_fileupload")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportFileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 신고 엔티티 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    /** 원본 파일명 */
    private String name;

    /** MIME 타입 */
    @Column(name = "type")
    private String type;

    /** 서버에 저장된 파일명 (DB 컬럼: re_name) */
    @Column(name = "re_name")
    private String reName;

    /** 실제 파일 경로(디렉토리) */
    private String path;

    /** 생성 시각 */
    @Column(name = "create_at")
    private LocalDateTime createAt;

    /** 썸네일 / 접근용 URL 등 */
    @Column(name = "thumb_path")
    private String thumbPath;

    /** 업로드 순서 */
    @Column(name = "upload_order")
    private Integer uploadOrder;

    /** 확장 경로 (url_path 테이블) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extend_file_path_id")
    private ReportExtendFilePath extendFilePath;
}
