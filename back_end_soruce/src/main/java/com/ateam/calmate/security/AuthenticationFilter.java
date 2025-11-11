package com.ateam.calmate.security;

import com.ateam.calmate.member.command.dto.RequestLoginDTO;
import com.ateam.calmate.member.command.dto.UserImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final Environment env;
    private final JwtFactory  jwtFactory;
    private  final CookieUtil cookieUtil;

    @Value("${app.cookie.domain}") private String cookieDomain; // 쿠키 도메인 설정(example.com)
    @Value("${token.refresh.expiration_time}")   private long refreshTtlMs;   // 리프레시 수명(ms)


    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                Environment env,  JwtFactory jwtFactory,
                                CookieUtil cookieUtil) {
        /* 설명. 새로 만든 프로바이더를 알고 있는 매니저를 인지시킴 */
        super(authenticationManager);
        this.env = env;
        this.jwtFactory = jwtFactory;
        this.cookieUtil = cookieUtil;
    }

    /* 필기. 인증 시작 */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLoginDTO creds = new ObjectMapper().readValue(request.getInputStream(), RequestLoginDTO.class);

            UsernamePasswordAuthenticationToken authRequest =
                    UsernamePasswordAuthenticationToken.unauthenticated(creds.getEmail(), creds.getPwd());
            setDetails(request, authRequest);// details 주입
            return this.getAuthenticationManager().authenticate(authRequest);

            /* 필기. 토큰 생성해서 manager에게 전달 */
//            return getAuthenticationManager().authenticate(
//                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPwd(), new ArrayList<>())
//            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /* 필기. 인증 완료 */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        /* 설명. JWT 토큰 제작 */
        /* 설명. 1. JWT 토큰 제작을 위한 재료 추출 */
        /* 설명. 프로바이더에서 반환한 내용 중 User의 내용은 principal로 저장되어 있음 */
        UserImpl user = (UserImpl) authResult.getPrincipal();
        String id = ((UserImpl) authResult.getPrincipal()).getUsername();

        // 권한정보 List<String>로 변환
        List<String> roles = authResult.getAuthorities().stream()
//                .map(role -> role.getAuthority())
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        /* 설명. 2. 재료를 활용한 JWT 토큰 제작(feat. build.gradle에 라이브러리 추가) */
//        Claims claims = Jwts.claims().setSubject(id);
//        claims.put("auth", roles);
//        claims.put("username", user.getMemberName());
//        claims.put("id", user.getId());
//        claims.put("birth", user.getBirth());
//        claims.put("memStsId", user.getMemStsId());
//
//        String token = Jwts.builder()
//                .setClaims(claims)      // 등록된 클레임 + 비공개 클레임
//                .setExpiration(new Date(System.currentTimeMillis()
//                        + Long.parseLong(env.getProperty("token.access.expiration_time"))))
//                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.access.secret"))
//                .compact();

        // 2) 리프레시 jti 생성
        String refreshJti = UUID.randomUUID().toString();

        //엑세스 토큰
        String accessToken = jwtFactory.createAccessToken(roles, user, refreshJti);

        //리프래시 토큰
        String refreshToken = jwtFactory.createRefreshToken(roles, user, refreshJti);



        // 5) 쿠키로 리프레시 전달
        ResponseCookie cookie = cookieUtil.createRefreshCookie(refreshToken, refreshTtlMs/1000, cookieDomain);

        response.addHeader("token", accessToken);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 정상 종료로  AuthenticationSuccessHandler 호출
        getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }



}
