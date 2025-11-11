package com.ateam.calmate.member.query.dto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestLoginwithAuthoritiesDTO {
    private Long id;
    private String email;
    private String memberName;
    private String birth;
    private String gender;
    private String nickName;
    private Integer bodyMetric;
    private String phone;
    private Long memStsId;
    private Double weight;
    private Double height;
    private LocalDateTime loginLockUntil;
    private Integer loginFailCnt;
    private String pwd;
    private List<Authority> authorities;


}
