package com.ateam.calmate.member.command.dto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestLoginwithAuthoritiesDTO {
    private Long id;
    private String email;
    private String memberName;
    private String birth;
    private Long memStsId;
    private Long crewId;
    private LocalDateTime loginLockUntil;
    private Integer loginFailCnt;
    private String pwd;
    private List<Authority> authorities;
}
