package com.ateam.calmate.community.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "extend_file_path")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostExtendFilePath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "url_path")
    private String urlPath;
}