package com.ateam.calmate.diary.command.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "extend_file_path_id")
    private Long extendFilePathId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;
}
