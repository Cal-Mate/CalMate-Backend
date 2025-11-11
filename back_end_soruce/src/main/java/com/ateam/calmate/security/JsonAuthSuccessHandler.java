package com.ateam.calmate.security;

import com.ateam.calmate.common.ResponseMessage;
import com.ateam.calmate.member.command.dto.ResoponseLoginDTO;
import com.ateam.calmate.member.command.dto.UserImpl;
import com.ateam.calmate.member.command.entity.Member;
import com.ateam.calmate.member.command.entity.UploadFile;
import com.ateam.calmate.member.command.repository.ProfileImageRepository;
import com.ateam.calmate.member.command.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JsonAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final ProfileImageRepository profileImageRepository;
    private final MemberService memberService;

    @Autowired
    public JsonAuthSuccessHandler(
            ProfileImageRepository profileImageRepository
            ,MemberService memberService){
        this.profileImageRepository = profileImageRepository;
        this.memberService = memberService;
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

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHttpStatus(status);
        responseMessage.setMessage(message);

        ResoponseLoginDTO resoponseLoginDTO = new ResoponseLoginDTO(
                id, userName, user.getNickName(), user.getBodyMetric(),
                user.getWeight(), user.getHeight(), user.getGender(),
                user.getBirth(), userEmail, profilePath, user.getPhone(),
                authorities
        );
        Map<String , Object> bodyPayload =  new HashMap<>();
        bodyPayload.put("user", resoponseLoginDTO);
        bodyPayload.put("extra", extra);

        responseMessage.setResult(bodyPayload);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(responseMessage);
        res.getWriter().write(json);

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
