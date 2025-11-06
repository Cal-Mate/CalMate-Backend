package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name="member1")
@Table(name = "Member")
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

    @Column(name = "memId", nullable = false)
    private String memId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "memPwd", nullable = false)
    private String memPwd;

    @Column(name = "memName", nullable = false)
    private String memName;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "signInDate", nullable = false)
    private LocalDate signInDate;

    @Column(name = "lastLogin")
    private LocalDateTime lastLogin;

    @Column(name = "climbCnt", nullable = false, insertable = false, updatable = true)
    private Long climbCnt;

    @Column(name = "banCnt")
    private Integer banCnt;

    @Column(name = "loginFailCnt")
    private Integer loginFailCnt;

    @Column(name = "quitDate")
    private LocalDateTime quitDate;

    @Column(name = "loginLockUntil")
    private LocalDateTime loginLockUntil;

    @Column(name = "score", nullable = false, insertable = false, updatable = true)
    private Integer score;

    @Column(name = "memRankId")
    private Long memRankId;

    @Column(name = "memStsId")
    private Long memStsId;

    @Column(name = "crewId")
    private Long crewId;

//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinColumn(name = "cumId")
//    private List<MemberAuthority> authorities;
}
