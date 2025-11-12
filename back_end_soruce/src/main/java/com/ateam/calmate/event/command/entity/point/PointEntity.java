package com.ateam.calmate.event.command.entity.point;

import com.ateam.calmate.event.command.entity.bingo.BingoBoardEntity;
import com.ateam.calmate.event.command.entity.gacha.GachaEventEntity;
import com.ateam.calmate.member.command.entity.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class PointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long pointId;

    @Column(name = "point")
    private Integer point;

    @Enumerated(EnumType.STRING)
    @Column(name = "distinction")
    private PointDistinction distinction; // EARN, USE

    // ========== 외래키 관계 ==========

    // Member (RESTRICT)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_point_member_id"))
    @JsonIgnore
    private Member member;

    @Column(name = "member_id", insertable = false, updatable = false)
    private Long memberId;

    // Diary (RESTRICT)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "diary_id", foreignKey = @ForeignKey(name = "fk_point_diary_id"))
//    @JsonIgnore
//    private Diary diary;

    @Column(name = "diary_id", insertable = false, updatable = false)
    private Integer diaryId;

    // Calendar (RESTRICT)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "calender_id", foreignKey = @ForeignKey(name = "fk_point_calender_id"))
//    @JsonIgnore
//    private Calendar calendar;

    @Column(name = "calender_id", insertable = false, updatable = false)
    private Long calenderId;

    // GachaEvent (선택적)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gacha_event_id", foreignKey = @ForeignKey(name = "fk_point_gacha_event_id"))
    @JsonIgnore
    private GachaEventEntity gachaEvent;

    @Column(name = "gacha_event_id", insertable = false, updatable = false)
    private Long gachaEventId;

    // BingoBoard (선택적)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bingo_board_id", foreignKey = @ForeignKey(name = "fk_point_bingo_board_id"))
    @JsonIgnore
    private BingoBoardEntity bingoBoard;

    @Column(name = "bingo_board_id", insertable = false, updatable = false)
    private Integer bingoBoardId;

    // ========== 생성일시 ==========
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public enum PointDistinction {
        EARN,    // 적립
        USE      // 사용
    }
}


