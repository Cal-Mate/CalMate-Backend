package com.ateam.calmate.member.command.dto;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestLoginDTO {
    private String email;
    private String pwd;
//    private String deviceFp;
}
