package com.ateam.calmate.event.command.dto.bingo;

import com.ateam.calmate.event.command.entity.bingo.BingoLineRewardEntity;
import com.ateam.calmate.event.enums.LineType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 빙고 라인을 표현하는 Value Object
 */
@Getter
@AllArgsConstructor
public class BingoLine {
    private LineType type;
    private Integer index; // ROW N, COL N인 경우 사용, DIAG인 경우 null

    public static BingoLine row(int rowIndex) {
        return new BingoLine(LineType.ROW, rowIndex);
    }

    public static BingoLine col(int colIndex) {
        return new BingoLine(LineType.COL, colIndex);
    }

    public static BingoLine diag1() {
        return new BingoLine(LineType.DIAG1, null);
    }

    public static BingoLine diag2() {
        return new BingoLine(LineType.DIAG2, null);
    }

    /**
     * Entity로 변환
     */
    public BingoLineRewardEntity toEntity(Integer boardId) {
        return BingoLineRewardEntity.builder()
                .boardId(boardId)
                .lineType(type)
                .lineIndex(index)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BingoLine bingoLine = (BingoLine) o;
        return type == bingoLine.type && Objects.equals(index, bingoLine.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, index);
    }

    @Override
    public String toString() {
        if (type == LineType.ROW) {
            return "가로 " + (index + 1) + "줄";
        } else if (type == LineType.COL) {
            return "세로 " + (index + 1) + "줄";
        } else if (type == LineType.DIAG1) {
            return "대각선 ↘";
        } else if (type == LineType.DIAG2) {
            return "대각선 ↙";
        }
        return "알 수 없는 라인";
    }
}
