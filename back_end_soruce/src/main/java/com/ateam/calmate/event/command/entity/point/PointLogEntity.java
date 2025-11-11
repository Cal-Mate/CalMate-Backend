package com.ateam.calmate.event.command.entity.point;

import com.ateam.calmate.event.enums.Distinction;
import com.ateam.calmate.event.enums.SourceDomain;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "point_log",
        indexes = {
                @Index(name = "idx_point_log_member_time", columnList = "member_id, created_at"),
                @Index(name = "idx_point_log_source", columnList = "source_domain, source_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_point_log_member_idem", columnNames = {"member_id", "idempotency_key"})
        }
)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class PointLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_log_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /** +적립 / -사용 */
    @Column(nullable = false)
    private Integer delta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Distinction distinction;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_domain", nullable = false, length = 16)
    private SourceDomain sourceDomain;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(length = 255)
    private String reason;

    @Column(name = "idempotency_key", length = 100)
    private String idempotencyKey;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}