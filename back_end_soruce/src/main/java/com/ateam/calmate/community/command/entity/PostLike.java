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
        name = "post_like",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_post_like_post_member",
                columnNames = {"post_id", "member_id"}
        )
)
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DB 컬럼: member_id
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    // DB 컬럼: post_id
    @Column(name = "post_id", nullable = false)
    private Integer postId;

    @Column(name = "like_created")
    private LocalDateTime likeCreated;

    @PrePersist
    void onCreate() {
        if (likeCreated == null) likeCreated = LocalDateTime.now();
    }
}