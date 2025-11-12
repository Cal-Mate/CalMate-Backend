package com.ateam.calmate.community.command.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CommunityMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nickname;

    private String email;

    private String pw;

    private String phone;

    private String gender;

    private String birth;

    private Double height;

    private Double weight;

    @Column(name = "body_metric")
    private Integer bodyMetric;

    private Integer point;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "login_failure_count")
    private Integer loginFailureCount;

    @Column(name = "login_lock_until")
    private LocalDateTime loginLockUntil;

    @Column(name = "quit_date")
    private LocalDateTime quitDate;

    // status, level 은 enum 또는 int 형태 그대로 유지
    private Long status;

    private Long level;

}