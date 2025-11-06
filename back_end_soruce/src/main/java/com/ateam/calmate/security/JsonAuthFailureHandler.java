package com.ateam.calmate.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class JsonAuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest req,
                                        HttpServletResponse res,
                                        AuthenticationException ex) throws IOException {

        int    status;
        String code;
        String message;
        Map<String, Object> extra = Map.of();

        if (ex instanceof MemberStatusAuthenticationException m) {
            status  = m.getHttpStatus();
            code    = m.getCode();
            message = m.getMessage();
            extra   = m.getExtra();
        } else if (ex instanceof BadCredentialsException) {
            status  = HttpServletResponse.SC_UNAUTHORIZED;
//            code    = "BAD_PASSWORD";
//            message = "비밀번호가 일치하지 않습니다.";
            code    = "Invalid";
            message = "일치하는 회원 정보가 없습니다.";
        } else if (ex instanceof DisabledException) {
            status  = HttpServletResponse.SC_FORBIDDEN;
            code    = "ACCOUNT_DISABLED";
            message = "비활성화된 계정입니다.";
        } else if (ex instanceof LockedException) {
            status  = HttpServletResponse.SC_FORBIDDEN;
            code    = "ACCOUNT_LOCKED";
            message = "잠긴 계정입니다.";
        } else {
            status  = HttpServletResponse.SC_UNAUTHORIZED;
            code    = "AUTH_FAILED";
            message = ex.getMessage();
        }

        // 필요하면 요청에서 로그인 식별자 가져오기 (폼로그인 기준)
//        String loginId = req.getParameter("username"); // formLogin().usernameParameter("email") 쓴다면 "email"
        // JSON 응답
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8");

        var sb = new StringBuilder()
                .append("{\"success\":false")
                .append(",\"code\":\"").append(code).append("\"")
                .append(",\"message\":\"").append(escapeJson(message)).append("\"");

//        if (loginId != null) sb.append(",\"loginId\":\"").append(escapeJson(loginId)).append("\"");
        if (!extra.isEmpty()) {
            // 간단 직렬화
            sb.append(",\"extra\":{");
            boolean first = true;
            for (var e : extra.entrySet()) {
                if (!first) sb.append(',');
                first = false;
                sb.append('"').append(escapeJson(e.getKey())).append("\":\"")
                        .append(escapeJson(String.valueOf(e.getValue()))).append('"');
            }
            sb.append('}');
        }
        sb.append('}');
        res.getWriter().write(sb.toString());
    }

    private String escapeJson(String s) { return s == null ? "" : s.replace("\"", "\\\""); }
}

