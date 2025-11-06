package com.ateam.calmate.member.command.dto;

import com.ateam.calmate.member.query.dto.BlackListDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseSignUpDTO {

    private boolean isDuplicateEmail; // 중복된 이메일 존재
    private boolean isExistingMember; // 이미 가입된 회원
    private boolean isBanMember; //블랙리스트 회원
    private BlackListDTO blackListDTO;
    private ResponseMemberDTO memberDTO;
}
