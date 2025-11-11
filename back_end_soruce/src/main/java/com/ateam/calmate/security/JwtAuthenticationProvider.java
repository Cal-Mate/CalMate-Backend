package com.ateam.calmate.security;


import com.ateam.calmate.member.command.dto.RequsetloginHisotry;
import com.ateam.calmate.member.command.dto.UserImpl;
import com.ateam.calmate.member.command.entity.Member;
import com.ateam.calmate.member.command.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/* 설명. Jwt 토큰 방식의 로그인을 구현할 때 UsernamePasswordAuthenticationToken을 처리 할 프로바이더 */
/* 설명. Service 계층의 UserDetailService를 활용해 DB에서 사용자 조회 후 인증 */
@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;  //평문과 암호화 된 다이제스트를 비교하기 위한 도구

    @Autowired
    public JwtAuthenticationProvider(MemberService memberService, PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
    }

    //일하겠습니다
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {


        // 1) 요청 메타데이터(details) 꺼내기
        Object d = authentication.getDetails();

        // (2) 커스텀을 안 썼어도 기본 WebAuthenticationDetails는 존재
        WebAuthenticationDetails web = (d instanceof WebAuthenticationDetails)
                ? (WebAuthenticationDetails) d : null;

        //클라이언트 ip 조회
        String clientIp = (web != null ? web.getRemoteAddress() : "unknown");

        /* 설명. 사용자가 로그인 시 입력한 값 */
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();

        /* 설명. DB에 있는 해당 회원의 정보 */
        UserDetails userDetails = memberService.loadUserByUsername(email);
        if(userDetails == null){
            throw new BadCredentialsException("일치하는 회원 정보가 없습니다.");
        }
        UserImpl user = (UserImpl) userDetails;

        // 아직 연속 접속 제한시간이 남아있을 경우(연속 비밀번호 오 입력)
        if(user.getLoginLockUntil() != null && LocalDateTime.now().isBefore(user.getLoginLockUntil()) ){
            throw new MemberStatusAuthenticationException(
                    "LOCKED_UNTIL",
                    "연속 비밀번호 오 입력으로 (" + user.getLoginLockUntil() + "까지 접속 불가)",
                    HttpServletResponse.SC_FORBIDDEN);
        }

        RequsetloginHisotry loginHistory = new RequsetloginHisotry();
        loginHistory.setCumId(user.getId());
        loginHistory.setClientIp(clientIp);
        loginHistory.setDateTime(LocalDateTime.now());

        if (!passwordEncoder.matches(pwd, userDetails.getPassword())) {
            loginHistory.setReason("Wrong password");
            memberService.updateInvlidPassword(loginHistory);
            throw new BadCredentialsException("일치하는 회원 정보가 없습니다.");
        }

        //회원 상태에 따라 토큰을 발행하지 않고 로그인 실패처리
        checkMemberStatus(user);

        //로그인시 포인트 흭득 로직
        memberService.calculatePoint(user.getId(), 1);

        //여기 까지 오면 정상 로그인 상태로 로그인 히스토리 저장
        memberService.updateCompleteLogin(loginHistory);

        /* 설명. 예외가 발생하지 않고 이 부분 이후가 실행되면 UserDetails에 담긴(인증된 회원정보) 정보로 Token 생성 */
        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }

    // 회원 상태에 따라 접속 제한 메시지 선택
    private void checkMemberStatus(UserImpl user) {
        // 1) 회원 상태별 선제 차단
        switch ((int) (long) user.getMemStsId()) {
            case 2 -> throw new MemberStatusAuthenticationException(
                    "WITHDRAWN", "탈퇴한 계정 입니다.", HttpServletResponse.SC_FORBIDDEN);
            case 3 -> {
                String until = String.valueOf(user.getLoginLockUntil());
                throw new MemberStatusAuthenticationException(
                        "LOCKED_UNTIL", until + " 까지 접속 불가 계정 입니다.",
                        HttpServletResponse.SC_FORBIDDEN,
                        Map.of("lockUntil", until));
            }
            case 4 -> throw new MemberStatusAuthenticationException(
                    "DORMANT", "휴면 상태 입니다.", HttpServletResponse.SC_FORBIDDEN);
            case 5 -> throw new MemberStatusAuthenticationException(
                    "BANNED", "접속 불가 계정 입니다.", HttpServletResponse.SC_FORBIDDEN);
            default -> {
                /* 통과 */
            }
        }
    }

    //어떤 토큰을 처리하겠습니다(필터에서 만든 토큰이 여기로 넘어옴)
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
