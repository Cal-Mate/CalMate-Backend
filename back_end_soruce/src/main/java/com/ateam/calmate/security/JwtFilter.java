package com.ateam.calmate.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/* 설명. 회원이 토큰을 들고왔을 때 토큰을 처리하는 필터 */
/* 설명. 한 번의 요청에 단 한번만 동작하는 필터로 주로 만듦 */
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        log.info("헤더의 Authorization에 담긴 내용 확인: {}", authorizationHeader);
        log.info("URI={} method={} contentType={}",
                request.getRequestURI(), request.getMethod(), request.getContentType());

        /* 설명. 토큰을 제대로 들고 왔다면 */
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);
            log.info("순수 토큰 내용: {}" , token);
            
            /* 설명. 토큰의 유효성 검사 */
            if(jwtUtil.validateToken(token)){
                Authentication authentication = jwtUtil.getAuthentication(token);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request,response);
    }
}
