package com.ateam.calmate.member.command.dto;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseStatusDTO {
    private int score;
    private boolean modifyMemberRank;
    private long memRankId;
    private String cumNm;
}
