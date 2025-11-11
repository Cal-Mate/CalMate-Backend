package com.ateam.calmate.event.query.dto.bingo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BingoLineJudge {

    @Data
    @Builder
    @AllArgsConstructor
    public static class BingoProgress {
        private int size;
        private int totalCells;
        private int checkedCount;
        private int completedLineCount; // 가로/세로/대각선 합
        private boolean completed;      // 비즈니스 정의에 따라: 전체 라인 완성 or N줄 이상
        private double progressPercent; // (checked / totalCells) * 100
    }

    /** 셀 좌표 표현(0-based) */
    @Data
    @AllArgsConstructor
    public static class Pos {
        private int r; // row
        private int c; // col
    }

    /**
     * @param size          보드 한 변 길이 (n)
     * @param checkedCells  체크 완료된 셀 좌표(0-based)
     * @param completionPolicy 라인 완료를 "보드 전체 라인 모두 완성"으로 볼지, 다른 조건(예: 1줄 이상)으로 볼지
     */
    public static BingoProgress judge(int size, List<Pos> checkedCells, CompletionPolicy completionPolicy) {
        int total = size * size;
        Set<Long> set = new HashSet<>(checkedCells.size() * 2);
        for (Pos p : checkedCells) {
            set.add(key(p.r, p.c));
        }

        int checked = set.size();

        // 가로/세로 라인 수
        int rowLines = 0;
        for (int r = 0; r < size; r++) {
            boolean full = true;
            for (int c = 0; c < size; c++) {
                if (!set.contains(key(r, c))) { full = false; break; }
            }
            if (full) rowLines++;
        }

        int colLines = 0;
        for (int c = 0; c < size; c++) {
            boolean full = true;
            for (int r = 0; r < size; r++) {
                if (!set.contains(key(r, c))) { full = false; break; }
            }
            if (full) colLines++;
        }

        // 대각선 2개
        boolean diag1 = true; // 좌상→우하
        for (int i = 0; i < size; i++) {
            if (!set.contains(key(i, i))) { diag1 = false; break; }
        }
        boolean diag2 = true; // 우상→좌하
        for (int i = 0; i < size; i++) {
            if (!set.contains(key(i, (size - 1) - i))) { diag2 = false; break; }
        }

        int completedLines = rowLines + colLines + (diag1 ? 1 : 0) + (diag2 ? 1 : 0);

        boolean completed = completionPolicy.isCompleted(size, completedLines, checked, total);
        double progress = total == 0 ? 0.0 : (checked * 100.0 / total);

        return BingoProgress.builder()
                .size(size)
                .totalCells(total)
                .checkedCount(checked)
                .completedLineCount(completedLines)
                .completed(completed)
                .progressPercent(round1(progress))
                .build();
    }

    /** 완성 기준 정책(비즈니스 별로 교체 가능) */
    public interface CompletionPolicy {
        boolean isCompleted(int size, int completedLines, int checkedCount, int totalCells);

        /** 예) 1줄만 완성되어도 보드 완료로 간주 */
        static CompletionPolicy oneLine() {
            return (size, lines, checked, total) -> lines >= 1;
        }

        /** 예) 모든 라인(가로n + 세로n + 대각2)이 모두 완성되어야 완료 */
        static CompletionPolicy allLines() {
            return (size, lines, checked, total) -> lines == (size + size + 2);
        }

        /** 예) 진행률 100%이면 완료로 간주(모든 칸 체크) */
        static CompletionPolicy allCells() {
            return (size, lines, checked, total) -> checked == total;
        }
    }

    private static long key(int r, int c) {
        // r,c가 0~32767 범위라고 가정하면 안전
        return (((long) r) << 32) ^ (c & 0xffffffffL);
    }

    private static double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }
}