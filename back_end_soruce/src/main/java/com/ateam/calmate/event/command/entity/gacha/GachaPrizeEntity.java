package com.ateam.calmate.event.command.entity.gacha;

import com.ateam.calmate.event.command.entity.BaseTimeEntity;
import com.ateam.calmate.event.enums.PrizeType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@Entity
@Table(name = "gacha_prize")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class GachaPrizeEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable=false)
    private String name;

    @Lob
    @Column(name="payload_json")
    private String payloadJson; // 필요하면 AttributeConverter로 Map/JsonNode 변환

    @Enumerated(EnumType.STRING)
    @Column(name="prize_type", nullable=false, length=20)
    private PrizeType prizeType;

    @Column(name="rank", nullable=false)
    private Integer rank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gacha_event_id", nullable = false)
    private GachaEventEntity event;

    @OneToOne(mappedBy = "prize", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private GachaQuantityEntity quantity;

    public void attachQuantity(GachaQuantityEntity q) {
        this.quantity = q;
        if (q != null) q.setPrize(this);
    }
}