package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LoginRecord")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "ip")
    private String ip;

    @Column(name = "preAddr", nullable = false)
    private String preAddr;

    @Column(name = "cumId", nullable = false)
    private Long cumId;
}
