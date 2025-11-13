package com.ateam.calmate.event.command.entity.bingo;

import com.ateam.calmate.event.enums.LineType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bingo_line_reward",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"board_id", "line_type", "line_index"})
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BingoLineRewardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_id", nullable = false)
    private Integer boardId;

    @Enumerated(EnumType.STRING)
    @Column(name = "line_type", nullable = false, length = 10)
    private LineType lineType;

    @Column(name = "line_index")
    private Integer lineIndex; // ROW/COL인 경우 인덱스, DIAG인 경우 null

    @Column(name = "rewarded_at")
    private LocalDateTime rewardedAt;

    @PrePersist
    protected void onCreate() {
        if (rewardedAt == null) {
            rewardedAt = LocalDateTime.now();
        }
    }
}
