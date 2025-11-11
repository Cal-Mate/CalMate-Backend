package com.ateam.calmate.member.command.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@ToString
public class RequestSignInDTO extends User {
    private Long id;
    private String email;
    private String memberName;
    private String birth;
    private Long memStsId;
    private LocalDateTime loginLockUntil;

    /* 설명. username, password, authorities 필드 대신 생성자로 대체 */
    public RequestSignInDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public void setDetails(RequestLoginwithAuthoritiesDTO user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.memberName = user.getMemberName();
        this.birth = user.getBirth();
        this.memStsId = user.getMemStsId();
        this.loginLockUntil = user.getLoginLockUntil();
    }
}
