package com.ateam.calmate.event.query.dto.gacha;

import com.ateam.calmate.event.enums.GrantStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GachaRewardGrantDTO {
    private Long id;
    private GrantStatus grantStatus;     // QUEUED / GRANTED / FAILED
    private LocalDateTime grantedAt;     // 지급 시각
    private LocalDateTime createdAt;
    private Long gachaSharedBoardId;     // 어떤 셀 당첨에 대한 지급인지
}