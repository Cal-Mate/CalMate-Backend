package com.ateam.calmate.event.command.repository.bingo;

import com.ateam.calmate.event.command.entity.bingo.BingoLineRewardEntity;
import com.ateam.calmate.event.enums.LineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BingoLineRewardRepository extends JpaRepository<BingoLineRewardEntity, Long> {

    /**
     * 특정 보드에서 특정 라인이 이미 보상을 받았는지 확인
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
           "FROM BingoLineRewardEntity r " +
           "WHERE r.boardId = :boardId " +
           "AND r.lineType = :lineType " +
           "AND (:lineIndex IS NULL AND r.lineIndex IS NULL OR r.lineIndex = :lineIndex)")
    boolean existsByBoardIdAndLine(
            @Param("boardId") Integer boardId,
            @Param("lineType") LineType lineType,
            @Param("lineIndex") Integer lineIndex
    );

    /**
     * 특정 보드의 모든 보상된 라인 조회
     */
    List<BingoLineRewardEntity> findAllByBoardId(Integer boardId);

    /**
     * 특정 보드의 보상 개수
     */
    long countByBoardId(Integer boardId);
}
