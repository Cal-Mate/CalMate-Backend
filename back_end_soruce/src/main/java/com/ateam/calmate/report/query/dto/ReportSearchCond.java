package com.ateam.calmate.report.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReportSearchCond {
    // 상태 필터: null(전체), true(처리완료), false(대기)
    private Boolean status;

    // 신고 유형(report_base.title) 키워드(부분일치)
    private String typeKeyword;

    // 통합 검색(신고자/피신고자 이름, 신고 제목)
    private String keyword;

    // 페이지네이션
    private int page = 0; // 0-base
    private int size = 20;
}
