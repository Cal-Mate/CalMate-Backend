package com.ateam.calmate.report.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "report_fileupload")
public class ReportFileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 다대일: report_fileupload.report_id → report.id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    @Setter
    private Report report;

    private String name;

    private String type;

    @Column(name = "re_name")
    private String reName;

    private String path;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "thumb_path")
    private String thumbPath;

    @Column(name = "upload_order")
    private Integer uploadOrder;

    // extend_file_path FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extend_file_path_id")
    private ReportExtendFilePath extendFilePath;
}
