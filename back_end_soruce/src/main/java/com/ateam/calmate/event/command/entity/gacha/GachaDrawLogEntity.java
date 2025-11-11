package com.ateam.calmate.event.command.entity.gacha;

import com.ateam.calmate.event.command.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@Table(name = "gacha_draw_log",
        indexes = @Index(name = "idx_draw_event_member", columnList = "gacha_event_id, member_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class GachaDrawLogEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="member_id", nullable=false)
    private Long memberId;

    @Column(name="gacha_event_id", nullable=false)
    private Long gachaEventId;

    @Column(name="gacha_shared_board_id", nullable=false)
    private Long gachaSharedBoardId;

    @Column(name="board_version", nullable=false)
    private Integer boardVersion;

    @Column(name="prize_id", nullable=false)
    private Long prizeId;

    @Column(name="prize_rank", nullable=false)
    private Integer prizeRank;

    @Column(name="result_code", nullable=false, length=30)
    private String resultCode; // e.g. "SUCCESS"
}
