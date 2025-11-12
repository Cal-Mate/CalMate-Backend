package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_failure_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginFailureRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "failure_datetime", nullable = false)
    private LocalDateTime date;

    @Column(name = "failure_ip")
    private String ip;

    @Column(name = "failure_reasone", nullable = false, length = 2000)
    private String reason;

    @Column(name = "member_id", nullable = false)
    private Long memberId;
}
