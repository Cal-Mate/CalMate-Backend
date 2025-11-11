package com.ateam.calmate.event.command.entity.bingo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bingo_fileupload",
        indexes = {
                @Index(name = "idx_cell_created", columnList = "bingo_cell_id, created_at")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BingoFileUploadEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "re_name")
    private String reName;

    private String path;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "extend_file_path_id")
    private Long extendFilePathId;

    @Column(name = "bingo_cell_id", nullable = false, insertable = false, updatable = false)
    private Integer bingoCellId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bingo_cell_id", nullable = false)
    private BingoCellEntity cell;
}