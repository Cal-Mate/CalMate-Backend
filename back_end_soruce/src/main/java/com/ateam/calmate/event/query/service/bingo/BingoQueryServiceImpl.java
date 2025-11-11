package com.ateam.calmate.event.query.service.bingo;

import com.ateam.calmate.event.query.dto.bingo.BingoBoardDTO;
import com.ateam.calmate.event.query.dto.bingo.BingoBoardSummaryDTO;
import com.ateam.calmate.event.query.dto.bingo.BingoCellDTO;
import com.ateam.calmate.event.query.dto.bingo.BingoLineJudge;
import com.ateam.calmate.event.query.mapper.BingoQueryRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class BingoQueryServiceImpl implements BingoQueryService {

    private final BingoQueryRepository mapper;

    public BingoQueryServiceImpl(BingoQueryRepository mapper) {
        this.mapper = mapper;
    }

    @Override
    public BingoBoardSummaryDTO getMonthlyBoardSummary(Long memberId, String yearMonth) {
        BingoBoardSummaryDTO dto = mapper.selectBoardSummaryByMemberAndMonth(memberId, yearMonth);
        if (dto == null) return null;

        // 필요 시 라인 계산(상세 조회 없이도 가능하게 하고 싶다면, 셀 데이터를 한 번 더 끌어오거나 테이블 함수/뷰로 해결)
        // 여기서는 진행률/줄수는 Mapper 집계 결과를 신뢰하고 그대로 리턴
        return dto;
    }

    @Override
    public BingoBoardDTO getBoardDetail(Integer boardId) {
        BingoBoardDTO dto = mapper.selectBoardDetailV2(boardId);
        enrichWithLineJudge(dto);
        return dto;
    }

    @Override
    public BingoBoardDTO getBoardDetailV1(Integer boardId) {
        BingoBoardDTO dto = mapper.selectBoardDetailV1(boardId);
        enrichWithLineJudge(dto);
        return dto;
    }

    private void enrichWithLineJudge(BingoBoardDTO dto) {
        if (dto == null || CollectionUtils.isEmpty(dto.getCells())) return;

        int size = dto.getSize();
        List<BingoLineJudge.Pos> checked = new ArrayList<>();
        for (BingoCellDTO c : dto.getCells()) {
            // DB가 1-based row/col이면 여기를 (c.getRow()-1, c.getCol()-1)로 보정
            if (Boolean.TRUE.equals(c.getChecked())) {
                checked.add(new BingoLineJudge.Pos(c.getRow(), c.getCol()));
            }
        }

        // 정책 선택: 한 줄 이상 완성되면 "완료"로 간주하는 예
        var progress = BingoLineJudge.judge(size, checked, BingoLineJudge.CompletionPolicy.oneLine());

        dto.setCheckedCount(progress.getCheckedCount());
        dto.setTotalCells(progress.getTotalCells());
        dto.setCompletedLineCount(progress.getCompletedLineCount());
        dto.setCompleted(progress.isCompleted());
        dto.setProgressPercent(progress.getProgressPercent());
    }
}