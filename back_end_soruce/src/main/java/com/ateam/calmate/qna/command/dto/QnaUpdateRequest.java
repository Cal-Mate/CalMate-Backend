package com.ateam.calmate.qna.command.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QnaUpdateRequest {
    private String title;
    private String contents;
}
