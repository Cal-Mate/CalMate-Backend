package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "AuthorityList")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthorityList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "authName", nullable = false)
    private String authName;

    @Column(name = "authDescribe")
    private String authDescribe;
}
