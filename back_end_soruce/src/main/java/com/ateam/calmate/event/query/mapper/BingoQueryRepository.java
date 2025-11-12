package com.ateam.calmate.event.query.mapper;

import com.ateam.calmate.event.query.dto.bingo.BingoBoardDTO;
import com.ateam.calmate.event.query.dto.bingo.BingoBoardSummaryDTO;
import com.ateam.calmate.event.query.dto.bingo.BingoCellDTO;
import com.ateam.calmate.event.query.dto.bingo.BingoFileUploadDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BingoQueryRepository {

    /** 월별 보드 요약 (한 사용자 = 매달 1보드 규칙) */
    BingoBoardSummaryDTO selectBoardSummaryByMemberAndMonth(
            @Param("memberId") Long memberId,
            @Param("yearMonth") String yearMonth // 예: "2025-11"
    );

    /** 보드 상세 (버전 A: Nested Select로 첨부를 가져옴 - 구현 간단) */
    BingoBoardDTO selectBoardDetailV1(@Param("boardId") Integer boardId);

    /** 내부용: 보드의 셀 목록 (V1에서 collection select로 사용) */
    List<BingoCellDTO> selectCellsByBoardId(@Param("boardId") Integer boardId);

    /** 내부용: 셀 하나의 첨부 목록 (V1에서 collection select로 사용) */
    List<BingoFileUploadDTO> selectUploadsByCellId(@Param("cellId") Integer cellId);

    /** 보드 상세 (버전 B: 조인 1방 - resultOrdered 이용해 중첩 컬렉션 그룹핑) */
    BingoBoardDTO selectBoardDetailV2(@Param("boardId") Integer boardId);
}