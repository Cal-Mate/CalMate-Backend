package com.ateam.calmate.event.command.entity.bingo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bingo_cell",
        indexes = {
                @Index(name = "idx_board_row_col", columnList = "bingo_board_id, row, col"),
                @Index(name = "idx_board_is_checked", columnList = "bingo_board_id, is_checked")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BingoCellEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "`row`")   // row는 예약어 가능성 → 백틱 매핑
    private Integer row;

    @Column(name = "`col`")
    private Integer col;

    private String label;

    @Column(name = "is_checked")
    private Boolean checked;

    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bingo_board_id", nullable = false)
    private BingoBoardEntity board;

    @OneToMany(mappedBy = "cell", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BingoFileUploadEntity> uploads;
}