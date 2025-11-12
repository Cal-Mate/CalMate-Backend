package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "point")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Point {
    @Id
    @Column(name = "point_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pointId;

    @Column(name = "point")
    private Integer point;

    @Enumerated(EnumType.STRING)
    @Column(name = "distinction")
    private Distinction distinction;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "diary_id")
    private Integer diaryId;

    @Column(name = "calender_id")
    private Long calenderId;

    @Column(name = "gacha_event_id")
    private Long gachaEventId;

    @Column(name = "bingo_board_id")
    private Integer bingoBoardId;

    @Column(name = "histoy_time", nullable = false, insertable = false )
    private Integer histoyTime;

    // ✅ enum 타입 정의 (DB의 ENUM('EARN','USE') 매핑)
    public enum Distinction {
        EARN, USE
    }
}
