package com.ateam.calmate.qna.query.mapper;

import com.ateam.calmate.qna.query.dto.QnaResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QnaMapper {

    List<QnaResponse> selectQnaList(@Param("limit") int limit,
                                    @Param("offset") int offset);

    List<QnaResponse> selectMyQnaList(@Param("memberId") Long memberId,
                                      @Param("limit") int limit,
                                      @Param("offset") int offset);

    QnaResponse selectQnaDetail(@Param("qnaId") Long qnaId);

    Long selectQnaIdByCommentId(Long commentId);
}
