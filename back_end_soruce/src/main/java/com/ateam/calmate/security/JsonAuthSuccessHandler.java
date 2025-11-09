package com.ateam.calmate.security;

import com.ateam.calmate.member.command.dto.UserImpl;
import com.ateam.calmate.member.command.entity.UploadFile;
import com.ateam.calmate.member.command.repository.ProfileImageRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JsonAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final ProfileImageRepository profileImageRepository;

    @Autowired
    public JsonAuthSuccessHandler(ProfileImageRepository profileImageRepository){
        this.profileImageRepository = profileImageRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req,
                                        HttpServletResponse res,
                                        Authentication authentication) throws IOException {

        // 기본 응답 값
        int    status  = HttpServletResponse.SC_OK;
        String code    = "OK";
        String message = "로그인에 성공했습니다.";

        // 로그인 사용자 정보
        String userEmail = authentication.getName();
        UserImpl user = (UserImpl) authentication.getPrincipal();
        Long id = user.getId();
        String userName = user.getMemberName();
        UploadFile profile = profileImageRepository.findByMemberId(id);
        String profilePath = profile != null ? profile.getFilePath() +  profile.getOriginalFileName() : null;
        List<String> authorities = toAuthorityList(authentication.getAuthorities());

        if(profilePath != null){
            String scheme = req.getScheme();         // http / https
            String serverName = req.getServerName(); // localhost
            int port = req.getServerPort();          // 8000

            String requestPath = scheme + "://" + serverName +  ":" + port;

            int index = profilePath.indexOf("/img");
            profilePath =  requestPath + profilePath.substring(index,profilePath.length());
        }


        // 추가 필드 예시(원하면 req에 Attribute로 담아 오거나, DB 조회해서 채우기)
        Map<String, Object> extra = Map.of(
                "loginAt", LocalDateTime.now().toString(),
                "sessionId", req.getSession(false) != null ? req.getSession(false).getId() : ""
        );
        String d = req.getRequestURL().toString();

        // JSON 응답
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8");

        StringBuilder sb = new StringBuilder()
                .append("{\"success\":true")
                .append(",\"code\":\"").append(code).append("\"")
                .append(",\"message\":\"").append(escapeJson(message)).append("\"")
                .append(",\"user\":{")
                .append("\"userId\":\"").append(escapeJson(id.toString())).append("\",")
                .append("\"userName\":\"").append(escapeJson(userName)).append("\",")
                .append("\"nickname\":\"").append(escapeJson(user.getNickName())).append("\",")
                .append("\"bodyMetric\":\"").append(escapeJson(user.getBodyMetric().toString())).append("\",")
                .append("\"weight\":\"").append(escapeJson(user.getWeight().toString())).append("\",")
                .append("\"height\":\"").append(escapeJson(user.getHeight().toString())).append("\",")
                .append("\"userEmail\":\"").append(escapeJson(userEmail)).append("\",")
                .append("\"profilePath\":\"").append(escapeJson(profilePath)).append("\",")
                .append("\"authorities\":").append(toJsonArray(authorities))
                .append("}");

        if (!extra.isEmpty()) {
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

    private List<String> toAuthorityList(Collection<? extends GrantedAuthority> authorities) {
        return authorities == null ? List.of()
                : authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    private String toJsonArray(List<String> values) {
        if (values == null || values.isEmpty()) return "[]";
        return values.stream()
                .map(v -> "\"" + escapeJson(v) + "\"")
                .collect(Collectors.joining(",", "[", "]"));
    }

    private String escapeJson(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }


}
