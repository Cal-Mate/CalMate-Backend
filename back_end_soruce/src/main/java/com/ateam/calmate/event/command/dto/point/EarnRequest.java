package com.ateam.calmate.event.command.dto.point;

import com.ateam.calmate.event.enums.SourceDomain;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EarnRequest {
    @Positive(message = "amount는 양수여야 합니다.")
    private int amount;

    @NotNull
    private SourceDomain sourceDomain;

    private Long sourceId;

    @Size(max = 255)
    private String reason;

    @NotBlank
    @Size(max = 100)
    private String idempotencyKey;
}