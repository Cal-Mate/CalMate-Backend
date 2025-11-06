package com.ateam.calmate.security;

import org.springframework.security.core.AuthenticationException;

import java.util.Map;

public class MemberStatusAuthenticationException extends AuthenticationException {
    private final String code;          // 예: "WITHDRAWN", "LOCKED_UNTIL"
    private final int httpStatus;       // 예: 403
    private final Map<String, Object> extra; // 잠금 해제 시각 등 추가 정보

    public MemberStatusAuthenticationException(String code, String message, int httpStatus) {
        this(code, message, httpStatus, Map.of());
    }
    public MemberStatusAuthenticationException(String code, String message, int httpStatus,
                                               Map<String, Object> extra) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
        this.extra = extra;
    }
    public String getCode() { return code; }
    public int getHttpStatus() { return httpStatus; }
    public Map<String, Object> getExtra() { return extra; }
}

