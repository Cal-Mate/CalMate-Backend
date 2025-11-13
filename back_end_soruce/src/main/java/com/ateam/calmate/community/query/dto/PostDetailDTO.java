package com.ateam.calmate.community.query.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostDetailDTO {
    private int id;
    private String title;
    private Long memberId;     // ✅ 게시글 작성자 memberId

    private String authorName;
    private String createdAt;
    private Integer visibility;
    private String tagName;
    private String content;
    private List<String> images;  // ✅ 단일 → 배열
    private Integer tagId;   // ✅ 추가

//    private String imageUrl;
    private int likes;
    private int comments;
    private boolean liked; // ✅ 추가

}