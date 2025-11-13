package com.ateam.calmate.community.query.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommentResponseDTO {
    private int id;
    private int postId;
    private String authorName;   // member.nickname
    private int memberId;         // ✅ 추가

    private String content;
    private String createdAt;    // create_at -> alias
    private Integer parentId;    // member_parent_comment_id -> alias

    private long likeCount;   // ✅ 추가
    private boolean liked;    // ✅ 추가
    private Integer visibility;


    private List<CommentResponseDTO> replies = new ArrayList<>();
}