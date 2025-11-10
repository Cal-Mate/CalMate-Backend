package com.ateam.calmate.member.command.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResoponseLoginDTO {
    private Long userId;
    private String userName;
    private String nickname;
    private Integer bodyMetric;
    private Double weight;
    private Double height;
    private String userEmail;
    private String profilePath;
    private List<String> authorities;
}
