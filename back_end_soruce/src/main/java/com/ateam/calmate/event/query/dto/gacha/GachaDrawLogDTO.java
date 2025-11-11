package com.ateam.calmate.event.query.dto.gacha;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GachaDrawLogDTO {
    private Long id;
    private LocalDateTime createdAt;
    private Long memberId;              // 뽑기한 회원
    private Long gachaSharedBoardId;    // 해당 보드 셀
    private Integer boardVersion;       // 당시 보드 버전
    private Long gachaPrizeId;          // 당첨 경품
}