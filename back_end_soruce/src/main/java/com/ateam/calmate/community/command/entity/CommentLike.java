package com.ateam.calmate.community.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "comment_like",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_comment_like_comment_member",
                columnNames = {"post_comment_id", "member_id"}
        )
)
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ DB 컬럼명과 매핑
    @Column(name = "post_comment_id", nullable = false)
    private Integer postCommentId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @PrePersist
    void onCreate() {
        if (createAt == null) createAt = LocalDateTime.now();
    }
}