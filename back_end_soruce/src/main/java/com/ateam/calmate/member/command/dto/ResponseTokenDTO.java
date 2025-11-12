package com.ateam.calmate.member.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseTokenDTO {
    private String refreshToken;
    private String accessToken;
}
