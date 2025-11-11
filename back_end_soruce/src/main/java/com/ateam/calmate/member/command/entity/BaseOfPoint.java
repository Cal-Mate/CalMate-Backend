package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "base_of_point")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseOfPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "point", nullable = false)
    private int point;
}
