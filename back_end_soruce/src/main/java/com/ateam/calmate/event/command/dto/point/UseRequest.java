package com.ateam.calmate.event.command.dto.point;

import com.ateam.calmate.event.enums.SourceDomain;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UseRequest {
    @Positive(message = "cost는 양수여야 합니다.")
    private int cost;

    @NotNull
    private SourceDomain sourceDomain;

    private Long sourceId;

    @Size(max = 255)
    private String reason;

    @NotBlank
    @Size(max = 100)
    private String idempotencyKey;
}