package com.ateam.calmate.member.command.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TokenDTO {
    private List<String> roles;
    private String memberName;
    private Long memberId;
    private String birth;
    private Long memberStatus;
    private String jti;
}
