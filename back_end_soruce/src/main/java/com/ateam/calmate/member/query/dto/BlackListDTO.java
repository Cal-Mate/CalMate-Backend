package com.ateam.calmate.member.query.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BlackListDTO {
    private LocalDateTime createDate;
    private Long adminId;
    private Long memberId;
}
