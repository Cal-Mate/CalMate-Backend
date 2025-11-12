package com.ateam.calmate.exerciseRecords.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "point")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExercisePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @Column(name = "point")
    private Integer amount;   // 적립/사용된 포인트 값

    @Enumerated(EnumType.STRING)
    @Column(name = "distinction")
    private Distinction distinction;   // EARN / USE

    @Column(name = "reason")
    private String reason;   // "EXERCISE", "DIARY" 등 발생 이유

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "histoy_time")
    private LocalDateTime historyTime;

    public enum Distinction {
        EARN, USE
    }
}
