package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "member")
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "pw", nullable = false)
    private String pw;

    @Column(name = "phone")
    private String phone;

    @Column(name = "gender", length = 1)
    private String gender;

    @Column(name = "birth")
    private LocalDate birth;

    @Column(name = "height", precision = 5, scale = 2)
    private BigDecimal height;

    @Column(name = "weight", precision = 5, scale = 2)
    private BigDecimal weight;

    @Column(name = "body_metric")
    private Integer bodyMetric;

    @Column(name = "point", insertable = false )
    private Integer point;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "login_failure_count", insertable = false)
    private Integer loginFailureCount;

    @Column(name = "login_lock_until")
    private LocalDateTime loginLockUntil;

    @Column(name = "quit_date")
    private LocalDateTime quitDate;

    @Column(name = "status", nullable = false)
    private Long status;

    @Column(name = "level", nullable = false)
    private Long level;

    @Column(name = "ban_cnt" , insertable = false)
    private Integer banCnt;
}
