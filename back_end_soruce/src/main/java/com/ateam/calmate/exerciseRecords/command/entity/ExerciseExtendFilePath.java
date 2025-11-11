package com.ateam.calmate.exerciseRecords.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "extend_file_path")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseExtendFilePath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_path", length = 255, nullable = false)
    private String urlPath;
}
