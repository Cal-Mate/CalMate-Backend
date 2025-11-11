package com.ateam.calmate.member.command.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestMemberDTO {

    private Long id;
    private String memId;
    private String email;
    private String nickname;
    private String memPwd;
    private String memName;
    private LocalDate birth;
    private String gender;
    private LocalDate signInDate;
}