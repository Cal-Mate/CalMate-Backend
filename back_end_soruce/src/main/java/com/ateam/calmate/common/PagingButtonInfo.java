package com.ateam.calmate.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PagingButtonInfo {
    private int currentPage;
    private int startPage;
    private int endPage;
    private int lastPage;

}
