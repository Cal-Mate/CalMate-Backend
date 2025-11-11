package com.ateam.calmate.report.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "extend_file_path")   // 실제 테이블 이름
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportExtendFilePath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                 // 파일경로 번호

    @Column(name = "url_path")
    private String urlPath;          // URL 경로

    @OneToMany(mappedBy = "extendFilePath")
    private List<ReportFileUpload> fileUploads;
}
