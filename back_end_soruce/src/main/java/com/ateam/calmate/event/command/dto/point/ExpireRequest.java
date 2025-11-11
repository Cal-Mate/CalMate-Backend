package com.ateam.calmate.event.command.dto.point;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpireRequest {
    @Positive(message = "amount는 양수여야 합니다.")
    private int amount;

    @Size(max = 255)
    private String reason;  // 예: "유효기간 만료"
}
