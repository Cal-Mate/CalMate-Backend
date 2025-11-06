package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MemberAuthority")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cumId", nullable = false)
    private Long cumId;

    @Column(name = "authId")
    private Long authId;

}
