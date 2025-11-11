package com.ateam.calmate.exerciseRecords.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "exercise_fileupload")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseFileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String name;

    @Column(length = 255)
    private String type;

    @Column(name = "re_name", length = 255)
    private String reName;

    @Column(length = 255)
    private String path;          // 지금은 안 써도 됨

    @Column(name = "thumb_path", length = 255)
    private String thumbPath;     // 지금은 안 써도 됨

    @Column(name = "upload_order")
    private Integer uploadOrder;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extend_file_path_id")
    private ExerciseExtendFilePath extendFilePath;

    @PrePersist
    void onCreate() {
        if (createAt == null) {
            createAt = LocalDateTime.now();
        }
    }
}
