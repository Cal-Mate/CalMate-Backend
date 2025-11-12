package com.ateam.calmate.report.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "post")
public class ReportPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(name = "content")
    private String content;

    // 0/1
    private Integer visibility;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "tag_id")
    private Integer tagId;

    public void setVisibility(Integer visibility) { this.visibility = visibility; }
}
