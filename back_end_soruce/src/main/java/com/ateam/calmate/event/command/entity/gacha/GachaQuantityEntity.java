package com.ateam.calmate.event.command.entity.gacha;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@Table(name = "gacha_quantity")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class GachaQuantityEntity {

    @Id
    @Column(name = "id")
    private Long id;  // prize.id와 동일

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private GachaPrizeEntity prize;

    @Column(name = "count", nullable = false)
    private Integer count;

    public static GachaQuantityEntity of(GachaPrizeEntity prize, int count) {
        return GachaQuantityEntity.builder()
                .prize(prize)
                .id(prize != null ? prize.getId() : null)
                .count(count)
                .build();
    }
}