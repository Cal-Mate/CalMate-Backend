package com.ateam.calmate.diary.command.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "diary_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String mime;

    private String path;

    @Column(name = "state")
    private String state;

    @Column(name = "original_file")
    private String originalFile;

    @Column(name = "`rename`")
    private String rename;

    // DB에서 NOT NULL이면 여기서도 맞춰줘야 함
    @Column(name = "extend_file_path_id", nullable = false)
    private Long extendFilePathId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    @JsonBackReference
    private Diary diary;
}
