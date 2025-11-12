package com.ateam.calmate.member.command.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@ToString
public class UserImpl extends User {
    private Long id;
    private String email;
    private String memberName;
    private String nickName;
    private String birth;
    private Integer bodyMetric;
    private Long memStsId;
    private Long crewId;
    private Double height;
    private Double weight;
    private String gender;
    private String phone;
    private Integer loginFailCnt;
    private LocalDateTime loginLockUntil;

    /* 설명. username, password, authorities 필드 대신 생성자로 대체 */
    public UserImpl(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public void setDetails(RequestLoginwithAuthoritiesDTO user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.memberName = user.getMemberName();
        this.birth = user.getBirth();
        this.memStsId = user.getMemStsId();
        this.crewId = user.getCrewId() == null ? 0L : user.getCrewId();
        this.loginLockUntil = user.getLoginLockUntil();
        this.loginFailCnt = user.getLoginFailCnt();
        this.weight = user.getWeight();
        this.height = user.getHeight();
        this.nickName = user.getNickName();
        this.bodyMetric = user.getBodyMetric();
        this.gender = user.getGender();
        this.phone = user.getPhone();
    }
}
