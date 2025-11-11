package com.ateam.calmate.ai.command.entity;

import com.ateam.calmate.ai.command.dto.MealType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_diet")
@Getter
@Setter
@NoArgsConstructor
public class AiDietEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private BigInteger id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MealType type;

    @Column(name = "total_kcal")
    private BigDecimal totalKcal;

    @Column(name = "kcal")
    private BigDecimal kcal;

    @Column(name = "total_protein")
    private BigDecimal totalProtein;

    @Column(name = "total_fat")
    private BigDecimal totalFat;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "name")
    private String name;

    @Column(name = "member_id")
    private BigInteger memberId;
}
