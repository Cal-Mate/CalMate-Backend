package com.ateam.calmate.qna.query.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class QnaCommentResponse {
    private Long id;               // BIGINT
    private String comment;
    private LocalDateTime createdAt;
    private Long qnaId;
    private Long memberId;
    private Long parentCommentId;  // nullable
}