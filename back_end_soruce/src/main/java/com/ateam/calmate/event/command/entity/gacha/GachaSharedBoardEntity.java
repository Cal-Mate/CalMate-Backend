package com.ateam.calmate.event.command.entity.gacha;

import com.ateam.calmate.event.command.entity.BaseTimeEntity;
import com.ateam.calmate.event.enums.CellStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(
        name = "gacha_shared_board",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_event_version_position",
                        columnNames = {"gacha_event_id","board_version","row","col"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class GachaSharedBoardEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 이벤트 ID (FK: gacha_event.id) */
    @Column(name = "gacha_event_id", nullable = false)
    private Long gachaEventId;

    /** 보드 버전 */
    @Column(name = "board_version", nullable = false)
    private Integer boardVersion;

    /** 행(1~10) — DDL 컬럼명이 row 이므로 그대로 매핑 */
    @Column(name = "row", nullable = false)
    private Integer row;  // 1..10

    /** 열(1~10) */
    @Column(name = "col", nullable = false)
    private Integer col;  // 1..10

    /** 경품 ID (FK: gacha_prize.id) — DDL상 NOT NULL */
    @Column(name = "gacha_prize_id", nullable = false)
    private Long gachaPrizeId;

    /** 셀 상태: COVERED / OPENED */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CellStatus status;

    /** 오픈한 회원 ID (nullable) */
    @Column(name = "opened_by_member_id")
    private Long openedByMemberId;

    /** 오픈 시각 (nullable) */
    @Column(name = "opened_at")
    private LocalDateTime openedAt;

    /** 낙관적 락 버전 */
    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    /* ================= 비즈니스 메서드 ================= */

    /** 셀을 오픈 처리 */
    public void open(Long memberId, Long prizeId) {
        if (this.status == CellStatus.OPENED) return;
        this.status = CellStatus.OPENED;
        this.openedByMemberId = memberId;
        this.openedAt = LocalDateTime.now();
        this.gachaPrizeId = prizeId; // 보상 노출/확정 시 사용
    }

    /** 셀을 덮개 상태로 되돌림(리셋 시) */
    public void cover(Long newPrizeId) {
        this.status = CellStatus.COVERED;
        this.openedByMemberId = null;
        this.openedAt = null;
        this.gachaPrizeId = newPrizeId;
    }

    /** 새 보드 생성 시 초기화용 팩토리 */
    public static GachaSharedBoardEntity coveredCell(Long eventId, int boardVersion, int row, int col, Long prizeId) {
        return GachaSharedBoardEntity.builder()
                .gachaEventId(eventId)
                .boardVersion(boardVersion)
                .row(row)
                .col(col)
                .gachaPrizeId(prizeId)     // 셔플/시드로 배정된 경품
                .status(CellStatus.COVERED)
                .version(0)
                .build();
    }
}