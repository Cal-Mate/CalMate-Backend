package com.ateam.calmate.qna.command.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaCreateRequest {
    private Long memberId;
    private String title;
    private String contents;
}
