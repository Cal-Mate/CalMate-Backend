package com.ateam.calmate.security;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

// HttpOnly + Secure + SameSite=strict 쿠키를 만들어 내려주는 유틸
//    - 리프레시 토큰은 항상 쿠키로 (JS 접근 불가)
//    - 액세스 토큰은 헤더로 (Bearer) 내려주는 패턴 권장
@Component
public class CookieUtil {

    // 리프레시 토큰을 담을 쿠키 이름(고정)
    public static final String REFRESH_COOKIE = "RT";

    // HttpOnly 쿠키 생성 (SameSite=strict) ,
    public ResponseCookie createRefreshCookie(String value, long maxAgeSeconds, String domain) {
        return ResponseCookie.from(REFRESH_COOKIE, value)        // 쿠키 이름과 값 설정
                .httpOnly(true)                                  // JS에서 접근 불가 → XSS로 읽기 차단
                .secure(true)                                    // HTTPS에서만 전송
                .sameSite("Strict")                              // 교차사이트 자동 전송 차단(강한 CSRF 방어)
                .path("/")                                       // 전체 경로에 대해 유효
                .domain(domain)                                  // 필요 시 서브도메인 공용 설정
                .maxAge(maxAgeSeconds)                           // 만료(브라우저 저장형)
                .build();
    }


    // HttpOnly 쿠키 생성 (SameSite=strict)
    public ResponseCookie createRefreshCookie(String value) {
        return ResponseCookie.from(REFRESH_COOKIE, value)        // 쿠키 이름과 값 설정
                .httpOnly(true)                                  // JS에서 접근 불가 → XSS로 읽기 차단
                .secure(true)                                    // HTTPS에서만 전송
                .sameSite("Strict")                              // 교차사이트 자동 전송 차단(강한 CSRF 방어)
                .path("/")                                       // 전체 경로에 대해 유효
                .build();
    }

    // 쿠키 삭제(로그아웃 시)
    public ResponseCookie deleteRefreshCookie() {
        return ResponseCookie.from(REFRESH_COOKIE, "")
                .httpOnly(true).secure(true).sameSite("Strict")
                .path("/").maxAge(0)              // maxAge=0 → 즉시 만료
                .build();
    }


    // 쿠키 삭제(로그아웃 시)
    public ResponseCookie deleteRefreshCookie(String domain) {
        return ResponseCookie.from(REFRESH_COOKIE, "")
                .httpOnly(true).secure(true).sameSite("Strict")
                .path("/").domain(domain).maxAge(0)              // maxAge=0 → 즉시 만료
                .build();
    }
}