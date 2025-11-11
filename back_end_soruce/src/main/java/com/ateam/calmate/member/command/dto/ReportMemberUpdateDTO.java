package com.ateam.calmate.member.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportMemberUpdateDTO {
    private Long memberId;
    private Long memStsId;  // 3: BAN, 5: BLACKLIST
    private Integer banCnt; // null이면 증가 없음
}
