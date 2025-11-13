package com.ateam.calmate.event.command.dto.gacha;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GachaDrawRequest {
    private Long eventId;
    private Long memberId;
    private Long cellId;
}

