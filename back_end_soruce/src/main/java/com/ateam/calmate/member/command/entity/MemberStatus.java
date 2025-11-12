package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status", nullable = false)
    private String status;
}
