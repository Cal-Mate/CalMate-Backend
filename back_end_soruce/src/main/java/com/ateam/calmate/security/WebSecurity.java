package com.ateam.calmate.security;

import com.ateam.calmate.member.command.service.RefreshTokenService;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

/* 설명. Spring Security 모듈 추가 후 default 로그인 페이지 제거 및 인가 설정 */
/* 필기. 조립 + 인가를 하는 부분 */
@Configuration
public class WebSecurity {

    private JwtAuthenticationProvider jwtAuthenticationProvider;
    private Environment env;        // JWT Token의 payload에 만료시간 만들기위해 추가
    private JwtFactory jwtFactory;
    private CookieUtil  cookieUtil;
    private RefreshTokenService refreshTokenService;
    @Value("${token.refresh.expiration_time}") long refreshTtlMs;

    @Autowired
    public WebSecurity(JwtAuthenticationProvider jwtAuthenticationProvider, Environment env,
                       JwtFactory jwtFactory, CookieUtil  cookieUtil,
                       RefreshTokenService refreshTokenService
    ) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.env = env;
        this.jwtFactory = jwtFactory;
        this.cookieUtil = cookieUtil;
        this.refreshTokenService = refreshTokenService;
    }

    /* 설명. 새로 생성한 프로바이더 bean으로 등록 */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                      JwtUtil jwtUtil,
                                                      JsonAuthFailureHandler failure,
                                                      JsonAuthSuccessHandler success) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // cors 설정
                // 기본 formLogin 필터 비활성화 (중복 방지)
                .formLogin(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authz ->
                                authz
                                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()     // 테스트를 위해 모든 권한 오픈
//                                        .requestMatchers("/**").permitAll()

                                        /* ② 관리자만 접근 가능 (hasRole/hasAuthority) ---------- */
                                        // hasRole("ADMIN")는 내부적으로 "ROLE_ADMIN" 권한을 찾습니다.
                                        // DB/토큰에 "ROLE_ADMIN"을 넣었다면 hasAuthority("ROLE_ADMIN")도 동일 효과
//                                        .requestMatchers("/admin/**").hasRole("ADMIN")
//
//                                        /* ③ 로그인된 회원만 접근 가능 (authenticated) --------- */
                                        .requestMatchers("/member/refresh").permitAll()
                                        .requestMatchers("/member/logout").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/img/**").permitAll()     // 이미지 경로는 누구나 접근 허용
                                        .requestMatchers("/health").permitAll()

                                        // 커뮤니티 조회는 모두 허용
                                        .requestMatchers(HttpMethod.GET, "/community/posts").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/community/post/*").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/community/post/*/comments").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/community/ranking").permitAll()

                                        // 커뮤니티 댓글 (작성/수정/삭제)
                                        .requestMatchers(HttpMethod.POST, "/community/post/*/comments").authenticated()
                                        .requestMatchers(HttpMethod.PATCH, "/community/post/*/comments/*").authenticated()
                                        .requestMatchers(HttpMethod.DELETE, "/community/post/*/comments/*").authenticated()

                                        // 게시글 (작성/수정/삭제)
                                        .requestMatchers(HttpMethod.POST, "/community/post").authenticated()     // 토큰때문에 안되면 permitAll()로 수정
                                        .requestMatchers(HttpMethod.PATCH, "/community/post/*").authenticated()  // 토큰때문에 안되면 permitAll()로 수정
                                        .requestMatchers(HttpMethod.DELETE, "/community/post/*").authenticated() // 토큰때문에 안되면 permitAll()로 수정

                                        // 게시글 및 댓글 좋아요
                                        .requestMatchers(HttpMethod.POST, "/community/post/*/like").authenticated()
                                        .requestMatchers(HttpMethod.POST, "/community/comment/*/like").authenticated()
//                                        .requestMatchers(
//                                                "/v3/api-docs/**",
//                                                "/swagger-ui/**",
//                                                "/swagger-ui.html",
//                                                "/swagger-resources/**",
//                                                "/webjars/**"
//                                        ).permitAll()
//                                authz.requestMatchers(HttpMethod.GET, "/member/**").permitAll()
//                                        .requestMatchers(HttpMethod.POST, "/users/**").permitAll()
//                                        .requestMatchers(HttpMethod.GET, "/test").permitAll()
//                                        .requestMatchers("/actuator/**").permitAll()
//                                        .requestMatchers(HttpMethod.GET, "/member/**").hasRole("ADMIN")
//                                        .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("ENTERPRISE","ADMIN")
//                                        .requestMatchers(HttpMethod.GET, "/test1/**", "/test2/**").hasAnyRole("ENTERPRISE","ADMIN")
                                        .anyRequest().authenticated()
                )
                /* 설명. Session 방식이 아닌 JWT Token 방식을 사용하겠다. */
                /* 설명. Session 방식이 아닌 JWT Token 방식으로 인증된 회원(Authentication)을 Local Thread로 지정하겠다. */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        /* 설명. 매니저를 지닌 필터 등록 */
        http.addFilter(getAuthenticationFilter(authenticationManager(),failure, success));

        /* 설명. 로그인 이후 토큰을 들고 온다면 JwtFilter를 추가해서 검증하도록 함 */
        /* 설명. UsernamePasswordAuthenticationFilter 보다 JwtFilter가 먼저 실행 하게 됌 */
        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // cors를 위해 추가
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173")); // * 금지(크리덴셜 쓰면)
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        // 브라우저가 읽을 수 있도록 노출
        config.setExposedHeaders(List.of("token", "Authorization"));
        config.setAllowCredentials(true); // 세션/쿠키 쓸 때
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /* 설명. Filter를 등록하기 위해 사용하는 메소드 */
    private Filter getAuthenticationFilter(AuthenticationManager authenticationManager
            , JsonAuthFailureHandler failure
            , JsonAuthSuccessHandler  success) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, env, jwtFactory,
                cookieUtil, refreshTokenService, refreshTtlMs);
        authenticationFilter.setAuthenticationFailureHandler(failure);
        authenticationFilter.setAuthenticationSuccessHandler(success);
        return authenticationFilter;
    }


}
