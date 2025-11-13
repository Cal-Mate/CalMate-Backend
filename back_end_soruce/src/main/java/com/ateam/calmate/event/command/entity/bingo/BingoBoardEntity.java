package com.ateam.calmate.event.command.entity.bingo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bingo_board")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BingoBoardEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private Integer size;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "completed_line_count")
    private Integer completedLineCount;

    @Column(name = "completed")
    private Boolean completed;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<BingoCellEntity> cells;
}