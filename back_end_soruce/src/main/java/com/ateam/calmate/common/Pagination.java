package com.ateam.calmate.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

@Slf4j
public class Pagination {
    public static PagingButtonInfo getPaginButtonInfo(Page page) {
        int currentPage = page.getNumber() + 1;     // 인덱스 개념 -> 실제 페이지에 보여질 번호의 개념으로 다시 변경
        int defaultButtonCount = 10;                //한 페이지에 보일 버튼의 갯수
        int startPage;                              // 한 페이지에 보여질 첫 버튼
        int endPage;                                // 한 페이지에 보여질 마지막 버튼

        startPage = (int) (Math.ceil(currentPage / (double)defaultButtonCount) - 1)
                * defaultButtonCount + 1;
        endPage = startPage + defaultButtonCount - 1;

        /* 설명. 이 아래쪽 조건믄들은 마지막 버튼이 축소되어야 될 2가지 경우 */
        /* 설명. 현재 페이지 버튼 그룹의 마지막 버튼이 전체의 마지막 페이지를 넘어가면 마지막 페이지가 endPage가 됨 */
        if(page.getTotalPages() < endPage){     // totalPage가 마지막 페이지보다 작으면
            endPage = page.getTotalPages();     // totalPage가 마지막 페이지 버튼이 된다
        }

        /* 설명. 게시글이 없거나 한 페이지가 채 차지도 않을만큼의 item이 존재할 때 */
        if(page.getTotalPages() == 0){          // 1페이지도 안된다면
            endPage = startPage;                // startPage가 곧 endPage가 된다.(1페이지 버튼만 표시)
        }

        return new PagingButtonInfo(currentPage, startPage, endPage, page.getTotalPages());
    }
}
