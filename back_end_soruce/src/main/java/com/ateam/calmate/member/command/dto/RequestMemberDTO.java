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
    private String email;
    private String nickname;
    private String phone;
    private String pw;
    private Double weight;
    private Double height;
    private int bodyMetric;
    private String name;
    private LocalDate birth;
    private String gender;
    private LocalDate createdAt;
}