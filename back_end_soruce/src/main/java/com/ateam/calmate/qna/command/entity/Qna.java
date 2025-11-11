package com.ateam.calmate.qna.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "qna")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Qna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String contents;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "qna", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QnaComment> comments = new ArrayList<>();
}
