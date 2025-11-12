package com.ateam.calmate.member.command.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseMemberDTO {

    private Long id;
    private String memId;
    private String email;
    private String nickname;
    private String memName;
    private LocalDate birth;
    private String gender;
    private LocalDate signInDate;
    private LocalDateTime lastLogin;
    private Long climbCnt;
    private Integer banCnt;
    private Integer loginFailCnt;
    private LocalDateTime quitDate;
    private LocalDateTime loginLockUntil;
    private Double height;
    private Double weight;
    private Integer score;
    private Long memRankId;
    private String rankName;
    private Long memStsId;
    private String profilePath;
    private Integer point;
}
