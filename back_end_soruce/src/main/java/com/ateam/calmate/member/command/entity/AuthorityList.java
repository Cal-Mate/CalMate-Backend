package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "authorites")
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

    @Column(name = "authurity", nullable = false)
    private String authurity;

    @Column(name = "description")
    private String description;
}
