package com.ateam.calmate.community.query.dto;


import lombok.Data;

@Data
public class CommunityRankDTO {
    private Long memberId;
    private String nickname;
    private int totalLikes;
}