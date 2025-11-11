package com.ateam.calmate.event.command.entity.gacha;

import com.ateam.calmate.event.command.entity.BaseTimeEntity;
import com.ateam.calmate.event.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "gacha_event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(of = "id")
@AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at", insertable = false, updatable = false))
public class GachaEventEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(name = "point", nullable = false)
    private Integer point;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private EventStatus status;

    @Column(name = "current_board_version")
    private Integer currentBoardVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gacha_reset_id") // gacha_reset.id
    private GachaResetEntity resetPolicy;

    @Builder.Default
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GachaPrizeEntity> prizes = new ArrayList<>();

    public void addPrize(GachaPrizeEntity p) { prizes.add(p); p.setEvent(this); }
    public void removePrize(GachaPrizeEntity p) { prizes.remove(p); p.setEvent(null); }
}