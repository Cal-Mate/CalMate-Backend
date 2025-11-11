package com.ateam.calmate.member.command.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestQuitMemberDTO {
    private Long id;
    private String memPwd;
}