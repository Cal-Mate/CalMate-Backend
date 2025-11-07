package com.ateam.calmate.community.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponseDTO {
    private int postId;
    private String title;
    private String content;
    private String createdAt;
    private String tagName;
    private String authorName;  // member의 닉네임
    private String imageUrl;    // 이미지 경로
    private int likeCount;
    private int commentCount;
}