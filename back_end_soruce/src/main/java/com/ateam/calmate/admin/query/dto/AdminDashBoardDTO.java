package com.ateam.calmate.admin.query.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdminDashBoardDTO {
    private Long post;
    private Long qna;
    private Long report;
    private Long point;
}