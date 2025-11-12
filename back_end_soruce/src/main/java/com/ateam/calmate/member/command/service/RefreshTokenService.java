package com.ateam.calmate.member.command.service;

import com.ateam.calmate.member.command.dto.ResponseTokenDTO;
import com.ateam.calmate.member.command.entity.RefreshTokenEntity;

import java.time.LocalDateTime;

public interface RefreshTokenService {
    RefreshTokenEntity saveNew(Long userId, String refreshRaw, String jti, LocalDateTime exp, String deviceFp);
    RefreshTokenEntity validateForRotation(String refreshRaw, String jtiClaim, Long userId, String deviceFp);
    void revoke(RefreshTokenEntity e);
    void revokeAllForUser(Long userId);

    ResponseTokenDTO refreshToken(String refreshRaw, String deviceFp);
}
