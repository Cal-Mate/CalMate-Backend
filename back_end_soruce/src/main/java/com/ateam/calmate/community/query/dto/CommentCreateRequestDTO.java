package com.ateam.calmate.community.query.dto;

import lombok.Data;

@Data
public class CommentCreateRequestDTO {
    private int postId;
    private int memberId;
    private String content;
    private Integer parentId;  // 대댓글이면 부모 댓글 id
    private Integer visibility;
}