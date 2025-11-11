package com.ateam.calmate.event.command.dto.point;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdjustRequest {
    @Min(value = -2_000_000_000) @Max(2_000_000_000)
    private int delta;      // +/− 허용

    @Size(max = 255)
    private String reason;

    @NotBlank
    @Size(max = 100)
    private String idempotencyKey;
}