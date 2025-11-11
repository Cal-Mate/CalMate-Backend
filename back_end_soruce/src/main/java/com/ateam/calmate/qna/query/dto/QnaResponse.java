package com.ateam.calmate.qna.query.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class QnaResponse {
    private Long id;               // BIGINT
    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private Long memberId;

    private List<QnaCommentResponse> comments;
}