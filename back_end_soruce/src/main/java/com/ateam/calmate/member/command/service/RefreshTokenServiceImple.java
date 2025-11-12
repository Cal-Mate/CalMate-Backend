package com.ateam.calmate.member.command.service;

import com.ateam.calmate.member.command.dto.ResponseTokenDTO;
import com.ateam.calmate.member.command.dto.TokenDTO;
import com.ateam.calmate.member.command.entity.RefreshTokenEntity;
import com.ateam.calmate.member.command.repository.RefreshTokenRepository;
import com.ateam.calmate.security.JwtFactory;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// ✅ 회전/재사용 감지/강제폐기 핵심 로직
@Service
public class RefreshTokenServiceImple implements RefreshTokenService {

    @Value("${token.refresh.expiration_time}")  private long refreshTtlMs;        // 리프레시 만료(ms) (예: 14일)

    private final RefreshTokenRepository repo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtFactory jwt;

    public RefreshTokenServiceImple(RefreshTokenRepository repo
    , BCryptPasswordEncoder  bCryptPasswordEncoder
    , JwtFactory jwt) {
        this.repo = repo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwt = jwt;
    }

    // 리프레시 토큰 레코드 저장(발급 시)
    public RefreshTokenEntity saveNew(Long userId, String refreshRaw, String jti, LocalDateTime exp, String deviceFp) {
        RefreshTokenEntity e = new RefreshTokenEntity();
        e.setMemberId(userId);                                          // 사용자 ID 설정
        e.setTokenHash(sha256(refreshRaw));                             // 원문은 저장 금지 → 해시만
        e.setJti(jti);                                                // jti 설정
        e.setIssuedAt(LocalDateTime.now());                           // 발급 시각
        e.setExpiresAt(exp);                                          // 만료 시각
        e.setRevoked(false);                                          // 초기값: 유효
        e.setDeviceFp(deviceFp);                                      // 디바이스 지문(옵션)
        return repo.save(e);                                          // 저장
    }



    // 리프레시 요청 시 검증 + 재사용 감지
    public RefreshTokenEntity validateForRotation(String refreshRaw, String jtiClaim, Long userId, String deviceFp) {
        RefreshTokenEntity e = repo.findByJti(jtiClaim)
                .orElseThrow(() -> new RuntimeException("Unknown jti"));    // jti가 DB에 없으면 의심/오류

        if (e.isRevoked()) throw new RuntimeException("Token revoked");     // 이미 폐기된 토큰 → 재사용 시도

        if (!e.getMemberId().equals(userId)) throw new RuntimeException("User mismatch"); // 사용자 불일치

        if (e.getExpiresAt().isBefore(LocalDateTime.now())) throw new RuntimeException("Expired"); // 만료

        if (!sha256(refreshRaw).equals(e.getTokenHash()))                      // 원문 해시 비교(위조/다른 원본)
            throw new RuntimeException("Hash mismatch");

        // (옵션) 디바이스 바인딩 체크: 발급 당시 deviceFp와 다르면 추가 인증/거부 정책 가능
        if (e.getDeviceFp() != null && deviceFp != null && !e.getDeviceFp().equals(deviceFp)) {
            throw new RuntimeException("Device fingerprint changed");
        }

        e.setLastUsedAt(LocalDateTime.now());                                // 사용 흔적 업데이트
        return repo.save(e);
    }

    // 이전 리프레시 레코드 폐기(회전)
    public void revoke(RefreshTokenEntity e) {
        e.setRevoked(true);                                                  // 폐기 플래그
        e.setRevokedAt(LocalDateTime.now());                                 // 폐기 시각
        repo.save(e);                                                        // 저장
    }

    // 사용자 모든 리프레시 강제 폐기(재사용 감지/계정 탈취 의심)
    public void revokeAllForUser(Long userId) {
        repo.findAllByMemberId(userId).forEach(rt -> {
            if (!rt.isRevoked()) {
                rt.setRevoked(true);
                rt.setRevokedAt(LocalDateTime.now());
                repo.save(rt);
            }
        });
    }

    @Override
    public ResponseTokenDTO refreshToken(String refreshRaw, String deviceFp) {



        // 1) JWT 파싱
        var parsed = jwt.parseRefresh(refreshRaw);
        Claims claims = parsed.getBody();                 // 서명/exp 검증

        Long userId = Long.parseLong(parsed.getBody().getSubject());             // sub
        List<String> roles = new ArrayList<>();
        String jti   = parsed.getBody().getId();                                  // jti

        String username = claims.get("username", String.class);
        long id = claims.get("id", Long.class);
        String birth = claims.get("birth", String.class);
        long memStsId = claims.get("memStsId", Long.class);
        String issuer = claims.getIssuer();              // "iss" 전용 단축 메서드


        Object authObject = claims.get("auth");
        if (authObject instanceof List<?>) {
            for (Object o : (List<?>) authObject) {
                if (o instanceof String) {
                    roles.add((String) o);
                }
            }
        }

        String newJti  = UUID.randomUUID().toString();                            // 새 jti
        TokenDTO token = new TokenDTO(
                roles, username, id, birth,
                memStsId, newJti
        );

        // 2) DB 검증(만료/폐기/해시/디바이스)
        var record = validateForRotation(refreshRaw, jti, userId, deviceFp);

        // 3) “회전”: 이전 토큰 폐기 + 새 토큰 발급
        revoke(record);                                                       // 이전 토큰 폐기
        String access  = jwt.createAccessToken(token);            // 새 액세스
        String refresh = jwt.createRefreshToken(token);                  // 새 리프레시
        LocalDateTime exp = LocalDateTime.now().plusSeconds(refreshTtlMs / 1000);
        saveNew(userId, refresh, newJti, exp, deviceFp);                      // 새 레코드 저장

        ResponseTokenDTO response = new ResponseTokenDTO(refresh, access);


        System.out.println("요청 RT");
        System.out.println(refreshRaw);
        System.out.println("반환 RT");
        System.out.println(refresh);

        return response;
    }


    // SHA-256 해시 생성 메서드
    public static String sha256(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(raw.getBytes());

            // 바이트 배열을 16진수 문자열로 변환
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}
