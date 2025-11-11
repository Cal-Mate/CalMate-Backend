package com.ateam.calmate.diary.query.mapper;

import com.ateam.calmate.diary.query.dto.DiaryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiaryMapper {

    List<DiaryResponse> selectDiaryList(
            @Param("memberId") Long memberId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    List<DiaryResponse> selectDiaryListByMemberAndDay(
            @Param("memberId") Long memberId,
            @Param("day") String day
    );

    DiaryResponse selectDiaryDetail(
            @Param("diaryId") Integer diaryId,
            @Param("memberId") Long memberId
    );
}
