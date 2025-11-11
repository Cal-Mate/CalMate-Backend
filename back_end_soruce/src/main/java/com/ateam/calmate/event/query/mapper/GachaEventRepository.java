package com.ateam.calmate.event.query.mapper;


import com.ateam.calmate.event.query.dto.gacha.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 공유 가챠 보드 조회 Mapper
 * - 최고 등급 경품 리셋 로직을 위한 조회 쿼리
 */
@Mapper
public interface GachaEventRepository {

    /**
     * 현재 활성 상태인 가챠 이벤트 조회
     */
    Optional<GachaEventDTO> findActiveGachaEvent();

    /**
     * 이벤트 단건 조회
     */
    Optional<GachaEventDTO> findEventById(@Param("gachaEventId") Long gachaEventId);

    /**
     * 이벤트별 최고 등급(가장 작은 rank) 경품 조회
     */
    Optional<GachaPrizeDTO> findTopRankPrizeByEvent(@Param("gachaEventId") Long gachaEventId);

    /**
     * 이벤트의 현재 활성 보드 버전 조회
     */
    Optional<Integer> findCurrentBoardVersion(@Param("gachaEventId") Long gachaEventId);

    /**
     * 현재 보드 버전의 모든 셀 조회 (10x10)
     */
    List<GachaSharedBoardDTO> findAllCellsByBoardVersion(
            @Param("gachaEventId") Long gachaEventId,
            @Param("boardVersion") Integer boardVersion
    );

    /**
     * 특정 보드 버전의 오픈된 셀 개수 조회
     */
    Integer countOpenedCells(
            @Param("gachaEventId") Long gachaEventId,
            @Param("boardVersion") Integer boardVersion
    );

    /**
     * 특정 보드 버전의 모든 셀 개수 조회 (보드 초기화 여부 판단용)
     */
    Integer countTotalCells(
            @Param("gachaEventId") Long gachaEventId,
            @Param("boardVersion") Integer boardVersion
    );

    /**
     * 경품별 남은 수량 조회
     */
    Optional<GachaQuantityDTO> findQuantityByPrizeId(@Param("prizeId") Long prizeId);

    /**
     * 이벤트의 리셋 정책 조회
     */
    Optional<GachaResetDTO> findResetPolicyByEvent(@Param("gachaEventId") Long gachaEventId);

    /**
     * 이벤트의 모든 경품 조회 (등급 순서)
     */
    List<GachaPrizeDTO> findAllPrizesByEventOrderByRank(@Param("gachaEventId") Long gachaEventId);

    /**
     * 특정 경품이 톱 랭크인지 확인
     */
    Integer checkIfTopRankPrize(
            @Param("gachaEventId") Long gachaEventId,
            @Param("prizeId") Long prizeId
    );

    /**
     * 보드 초기화 전 이전 버전의 셀들 조회 (백업용)
     */
    List<GachaSharedBoardDTO> findPreviousBoardCells(
            @Param("gachaEventId") Long gachaEventId,
            @Param("boardVersion") Integer boardVersion
    );

    /**
     * 리셋 정책이 TOP_RANK이고 최고 등급 경품인지 확인
     */
    Boolean isTopRankResetPolicy(
            @Param("gachaEventId") Long gachaEventId,
            @Param("prizeId") Long prizeId
    );

}
