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

@Configuration
public class WebSecurity {

    private JwtAuthenticationProvider jwtAuthenticationProvider;
    private Environment env;
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
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(authz ->
                                authz
                                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
//                                        .requestMatchers("/**").permitAll()


                                        .requestMatchers("/member/refresh").permitAll()
                                        .requestMatchers("/member/logout").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/img/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                                        .requestMatchers("/health").permitAll()

                                        .requestMatchers(HttpMethod.GET, "/community/posts").permitAll()
                                        .requestMatchers("/api/calendar/**").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/community/post/*").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/community/post/*/comments").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/community/ranking").permitAll()

                                        .requestMatchers(HttpMethod.POST, "/community/post/*/comments").authenticated()
                                        .requestMatchers(HttpMethod.PATCH, "/community/post/*/comments/*").authenticated()
                                        .requestMatchers(HttpMethod.DELETE, "/community/post/*/comments/*").authenticated()

                                        .requestMatchers(HttpMethod.POST, "/community/post").authenticated()
                                        .requestMatchers(HttpMethod.PATCH, "/community/post/*").authenticated()
                                        .requestMatchers(HttpMethod.DELETE, "/community/post/*").authenticated()

                                        .requestMatchers(HttpMethod.POST, "/community/comment/*/like").authenticated()

                                        // 빙고 이벤트 API (테스트를 위해 임시로 모두 허용)
                                        .requestMatchers(HttpMethod.GET, "/api/bingo/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/bingo/**").permitAll()
                                        .requestMatchers(HttpMethod.DELETE, "/api/bingo/**").permitAll()

                                        // 가챠 이벤트 API (테스트를 위해 임시로 모두 허용)
                                        .requestMatchers("/api/gacha/**").permitAll()

                                        // 포인트 API (테스트를 위해 임시로 모두 허용)
                                        .requestMatchers("/api/event/points/**").permitAll()

                                        // 업로드 파일 접근
                                        .requestMatchers("/uploads/**").permitAll()

                                        // WebSocket 엔드포인트 허용
                                        .requestMatchers("/ws-gacha/**").permitAll()

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
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        http.addFilter(getAuthenticationFilter(authenticationManager(),failure, success));

        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        config.setExposedHeaders(List.of("token", "Authorization"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

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