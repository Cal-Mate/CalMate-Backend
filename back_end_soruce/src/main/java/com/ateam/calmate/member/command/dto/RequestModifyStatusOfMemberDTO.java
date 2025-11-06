package com.ateam.calmate.member.command.dto;


import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RequestModifyStatusOfMemberDTO {
    private Long cumId;
    // key : 등급 기준 점수, value : primaryKey(id)
    private Map<Integer,Long> baseMemberRanks;
    private int summaryScore;
}
