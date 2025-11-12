package com.ateam.calmate.event.query.service.bingo;

import com.ateam.calmate.event.query.dto.bingo.BingoBoardDTO;
import com.ateam.calmate.event.query.dto.bingo.BingoBoardSummaryDTO;

public interface BingoQueryService {
    BingoBoardSummaryDTO getMonthlyBoardSummary(Long memberId, String yearMonth);
    BingoBoardDTO getBoardDetail(Integer boardId);       // 기본 V2
    BingoBoardDTO getBoardDetailV1(Integer boardId);     // 필요 시 강제 V1
}