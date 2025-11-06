package com.ateam.calmate.member.command.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequsetloginHisotry {
    private String clientIp;
    private Long cumId;
    private String reason;
    private LocalDateTime dateTime;
}
