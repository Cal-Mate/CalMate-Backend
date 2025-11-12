package com.ateam.calmate.community.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "point")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPointLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long pointId;

    private Integer point;

    @Enumerated(EnumType.STRING)
    private Distinction distinction; // EARN or USE

    private String reason;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "histoy_time")
    private LocalDateTime historyTime;

    @PrePersist
    public void prePersist() {
        if (historyTime == null) {
            historyTime = LocalDateTime.now();
        }
    }

//    public CommunityPointLog(Integer point, Distinction distinction, String reason, Long memberId) {
//        this.point = point;
//        this.distinction = distinction;
//        this.reason = reason;
//        this.memberId = memberId;
//    }

    public enum Distinction {
        EARN, USE
    }
}