package com.ateam.calmate.qna.command.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaCommentCreateRequest {
    private Long memberId;
    private Long parentCommentId;   // 옵션
    private String comment;
}
