package com.ateam.calmate.report.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private final long total;
    private final int page;
    private final int size;
    private final List<T> items;
}
