package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LoginFailureRecord")
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

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "ip")
    private String ip;

    @Column(name = "reason", nullable = false, length = 2000)
    private String reason;

    @Column(name = "cumId", nullable = false)
    private Long cumId;
}
