package com.ateam.calmate.security;

import com.ateam.calmate.member.command.dto.TokenDTO;
import com.ateam.calmate.member.command.dto.UserImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtFactory {
    // 서명키(예시는 간단 문자열, 실제는 환경변수/Secret Manager 사용)
    @Value("${token.access.secret}") private String accessSecret;     // 액세스 토큰 서명 비밀
    @Value("${token.refresh.secret}") private String refreshSecret;   // 리프레시 토큰 서명 비밀
    @Value("${token.access.expiration_time}")   private long accessTtlMs;         // 액세스 만료(ms) (예: 10분)
    @Value("${token.refresh.expiration_time}")  private long refreshTtlMs;        // 리프레시 만료(ms) (예: 14일)

    public String createAccessToken(List<String> roles, UserImpl user,String newJti) {

        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + accessTtlMs);

        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put("auth", roles);
        claims.put("username", user.getMemberName());
        claims.put("id", user.getId());
        claims.put("birth", user.getBirth());
        claims.put("memStsId", user.getMemStsId());

        String token = Jwts.builder()
                .setClaims(claims)      // 등록된 클레임 + 비공개 클레임
                .setExpiration(exp)
                .setId(newJti)      // jti: 리프레시의 고유 식별자(회전에 중요)
                .signWith(SignatureAlgorithm.HS512, accessSecret)
                .compact();

        return token;
    }

    public String createRefreshToken(List<String> roles, UserImpl user,String newJti) {

        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + refreshTtlMs);

        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put("auth", roles);
        claims.put("username", user.getMemberName());
        claims.put("id", user.getId());
        claims.put("birth", user.getBirth());
        claims.put("memStsId", user.getMemStsId());

        String token = Jwts.builder()
                .setClaims(claims)      // 등록된 클레임 + 비공개 클레임
                .setExpiration(exp)
                .setId(newJti)      // jti: 리프레시의 고유 식별자(회전에 중요)
                .signWith(SignatureAlgorithm.HS512, refreshSecret)
                .compact();


        return token;
    }


    public String createAccessToken(TokenDTO tokenDTO) {

        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + accessTtlMs);

        Claims claims = Jwts.claims().setSubject(tokenDTO.getMemberId().toString());
        claims.put("auth", tokenDTO.getRoles());
        claims.put("username", tokenDTO.getMemberName());
        claims.put("id", tokenDTO.getMemberId());
        claims.put("birth", tokenDTO.getBirth());
        claims.put("memStsId", tokenDTO.getMemberStatus());

        String token = Jwts.builder()
                .setClaims(claims)      // 등록된 클레임 + 비공개 클레임
                .setExpiration(exp)
                .setId(tokenDTO.getJti())      // jti: 리프레시의 고유 식별자(회전에 중요)
                .signWith(SignatureAlgorithm.HS512, accessSecret)
                .compact();

        return token;
    }

    public String createRefreshToken(TokenDTO tokenDTO) {

        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + refreshTtlMs);

        Claims claims = Jwts.claims().setSubject(tokenDTO.getMemberId().toString());
        claims.put("auth", tokenDTO.getRoles());
        claims.put("username", tokenDTO.getMemberName());
        claims.put("id", tokenDTO.getMemberId());
        claims.put("birth", tokenDTO.getBirth());
        claims.put("memStsId", tokenDTO.getMemberStatus());

        String token = Jwts.builder()
                .setClaims(claims)      // 등록된 클레임 + 비공개 클레임
                .setExpiration(exp)
                .setId(tokenDTO.getJti())      // jti: 리프레시의 고유 식별자(회전에 중요)
                .signWith(SignatureAlgorithm.HS512, refreshSecret)
                .compact();


        return token;
    }

    // 파싱(검증 포함) - 리프레시용
    public Jws<Claims> parseRefresh(String jwt) {
        byte[] keyBytes = Decoders.BASE64.decode(refreshSecret);
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .build()
                .parseClaimsJws(jwt);
    }
}
