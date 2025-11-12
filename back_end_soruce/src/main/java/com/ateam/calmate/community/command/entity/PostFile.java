package com.ateam.calmate.community.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "post_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String url;

    @Column(name = "mime_type")
    private String mimeType;

    private String path;

    private String state;

    @Column(name = "re_name")
    private String reName;

    @Column(name = "post_id")
    private Integer postId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "extend_file_path_id", referencedColumnName = "id")
    private PostExtendFilePath extendFilePath;

    // @Column(name = "extend_file_path_id")
    // private Integer extendFilePathId;
}
