package com.ateam.calmate.report.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "post_comment")
public class ReportComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content")
    private String content;

    @Column(name = "create_at")
    private java.time.LocalDateTime createAt;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_parent_comment_id")
    private Integer parentCommentId;

    @Column(name = "visibility")
    private Integer visibility;

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }
}
