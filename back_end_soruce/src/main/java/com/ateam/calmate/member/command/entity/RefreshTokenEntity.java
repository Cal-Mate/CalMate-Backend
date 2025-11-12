package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefreshTokenEntity {

    /** 기본키 (AUTO_INCREMENT) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 해시 처리된 리프레시 토큰 (NOT NULL) */
    @Column(name = "token_hash", nullable = false, length = 128)
    private String tokenHash;

    /** JWT의 jti (unique) */
    @Column(name = "jti", unique = true, length = 64)
    private String jti;

    /** 발급 시각 (DEFAULT CURRENT_TIMESTAMP) */
    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    /** 만료 시각 */
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    /** 폐기 여부 (tinyint → boolean 매핑됨) */
    @Column(name = "revoked", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean revoked;

    /** 폐기 시각 */
    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    /** 디바이스 지문 */
    @Column(name = "device_fp")
    private String deviceFp;

    /** 마지막 사용 시각 (DEFAULT CURRENT_TIMESTAMP) */
    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    /** 접속 IP */
    @Column(name = "ip")
    private String ip;

    /** 멤버 ID (FK, NOT NULL) */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

}
