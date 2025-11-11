package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ProfileOfMember")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileOfMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dirPath")
    private String dirPath;

    @Column(name = "filePath")
    private String filePath;

    @Column(name = "cumId")
    private Long cumId;
}
