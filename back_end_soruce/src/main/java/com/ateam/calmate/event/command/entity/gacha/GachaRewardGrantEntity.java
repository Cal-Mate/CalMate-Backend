package com.ateam.calmate.event.command.entity.gacha;

import com.ateam.calmate.event.command.entity.BaseTimeEntity;
import com.ateam.calmate.event.enums.GrantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "gacha_reward_grant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class GachaRewardGrantEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="member_id", nullable=false)
    private Long memberId;

    @Column(name="prize_id", nullable=false)
    private Long prizeId;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable=false, length=20)
    private GrantStatus status; // READY, DONE ...

    @Column(name="granted_at")
    private LocalDateTime grantedAt;

    public void markGranted() {
        this.status = GrantStatus.FAILED;
        this.grantedAt = LocalDateTime.now();
    }
}