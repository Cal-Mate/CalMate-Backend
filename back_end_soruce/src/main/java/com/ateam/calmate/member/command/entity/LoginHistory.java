package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "login_date", nullable = false)
    private LocalDateTime date;

    @Column(name = "come_in_ip")
    private String ip;

    @Column(name = "before_path", nullable = false)
    private String preAddr;

    @Column(name = "member_id", nullable = false)
    private Long memberId;
}
