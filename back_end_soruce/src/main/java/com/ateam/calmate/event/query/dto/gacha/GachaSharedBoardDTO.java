package com.ateam.calmate.event.query.dto.gacha;

import com.ateam.calmate.event.enums.CellStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GachaSharedBoardDTO {
    private Long id;
    private Long gachaEventId;      // 이벤트
    private Integer boardVersion;   // 보드 버전
    private Integer row;            // 1~10
    private Integer col;            // 1~10
    private Long gachaPrizeId;      // 배치된 경품
    private CellStatus status;      // COVERED / OPENED
    private Long openedByMemberId;  // 오픈한 회원 (nullable)
    private LocalDateTime openedAt; // 오픈시각 (nullable)
    private Integer version;        // 낙관적 락 버전
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}