package com.ateam.calmate.dietManagement.command.entity;

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
public class MealPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @Column(name = "point")
    private Integer amount; // 포인트 수량

    @Enumerated(EnumType.STRING)
    @Column(name = "distinction")
    private Distinction distinction; // EARN / USE

    @Column(name = "reason")
    private String reason; // "MEAL" 등 이유

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "histoy_time")
    private LocalDateTime historyTime;

    public enum Distinction {
        EARN, USE
    }
}
