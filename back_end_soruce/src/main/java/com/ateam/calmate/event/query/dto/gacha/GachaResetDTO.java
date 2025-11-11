package com.ateam.calmate.event.query.dto.gacha;

import com.ateam.calmate.event.enums.PolicyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class GachaResetDTO {
    private Long id;
    private String name;                     // 정책명
    private PolicyType policyType;           // TOP_RANK / TIME / MANUAL / ETC
    private Map<String, Object> policyJson;  // 세부 정책(JSON)
    private LocalDateTime createdAt;
}