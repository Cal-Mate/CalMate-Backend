package com.ateam.calmate.event.command.entity.gacha;

import com.ateam.calmate.event.enums.PolicyType;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@Table(name = "gacha_reset")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class GachaResetEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable=false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="policy_type", nullable=false, length=20)
    private PolicyType policyType;
}