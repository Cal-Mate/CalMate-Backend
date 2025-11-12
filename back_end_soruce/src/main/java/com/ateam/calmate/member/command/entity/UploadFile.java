package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity(name = "upload_file1")
@Table(name = "upload_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UploadFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "State")
    private String state;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "re_file_name")
    private String reFileName;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "extend_file_path_id", nullable = false)
    private Long extendFilePathId;
}
