package com.ateam.calmate.event.command.service.bingo;

import com.ateam.calmate.event.command.dto.bingo.BingoLine;
import com.ateam.calmate.event.command.dto.bingo.CheckCellWithUploadCommand;
import com.ateam.calmate.event.command.dto.bingo.CheckCellWithUploadResult;
import com.ateam.calmate.event.command.entity.bingo.BingoBoardEntity;
import com.ateam.calmate.event.command.entity.bingo.BingoCellEntity;
import com.ateam.calmate.event.command.entity.bingo.BingoFileUploadEntity;
import com.ateam.calmate.event.command.port.FileStoragePort;
import com.ateam.calmate.event.command.port.PointsPort;
import com.ateam.calmate.event.command.repository.bingo.BingoBoardCommandRepository;
import com.ateam.calmate.event.command.repository.bingo.BingoCellCommandRepository;
import com.ateam.calmate.event.command.repository.bingo.BingoFileUploadCommandRepository;
import com.ateam.calmate.event.command.repository.bingo.BingoLineRewardRepository;
import com.ateam.calmate.event.query.dto.bingo.BingoLineJudge;
import com.ateam.calmate.member.command.entity.Point;
import com.ateam.calmate.member.command.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BingoCommandServiceImpl implements BingoCommandService {

    private final BingoBoardCommandRepository boardRepo;
    private final BingoCellCommandRepository cellRepo;
    private final BingoFileUploadCommandRepository uploadRepo;
    private final BingoLineRewardRepository lineRewardRepo;
    private final FileStoragePort fileStorage;
    private final PointsPort pointsPort;
    private final PointRepository pointRepository;

    @Value("${bingo.points.perLine:50}")
    private int perLinePoint;

    @Value("${bingo.points.boardBonus:500}")
    private int boardBonusPoint;

    @Value("${bingo.default-extend-file-path-id:1}")
    private Long defaultExtendFilePathId;

    @Override
    @Transactional
    public CheckCellWithUploadResult checkCellWithUpload(CheckCellWithUploadCommand cmd, InputStream fileStream) {
        BingoCellEntity cell = cellRepo.findByIdForUpdate(cmd.getCellId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 셀입니다."));
        BingoBoardEntity board = boardRepo.findByIdForUpdate(cell.getBoard().getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 보드입니다."));

        if (!board.getMemberId().equals(cmd.getMemberId()))
            throw new IllegalArgumentException("본인 보드만 수정할 수 있습니다.");

        // 파일 저장
        String directory = "bingo/" + board.getId() + "/" + cell.getId();
        var saved = fileStorage.save(directory, cmd.getOriginalFilename(), cmd.getContentType(), fileStream, cmd.getFileSize());

        BingoFileUploadEntity upload = BingoFileUploadEntity.builder()
                .cell(cell)
                .name(cmd.getOriginalFilename())
                .mimeType(cmd.getContentType())
                .reName(saved.storedFileName())
                .path(saved.relativePath())
                .createdAt(LocalDateTime.now())
                .extendFilePathId(resolveExtendFilePathId(cmd.getExtendFilePathId()))
                .build();
        uploadRepo.save(upload);

        boolean wasAlreadyChecked = Boolean.TRUE.equals(cell.getChecked());

        // 셀 체크 처리
        if (!wasAlreadyChecked) {
            cell.setChecked(true);
            cell.setCheckedAt(LocalDateTime.now());
        }

        // 라인 판정 (동시성 문제 방지를 위해 락 사용)
        List<BingoLineJudge.Pos> checked = new ArrayList<>();
        var allCells = cellRepo.findAllByBoardIdForUpdate(board.getId());
        for (BingoCellEntity c : allCells) {
            if (!Boolean.TRUE.equals(c.getChecked())) {
                continue;
            }
            Integer rowIdx = toZeroBased(c.getRow());
            Integer colIdx = toZeroBased(c.getCol());
            if (rowIdx == null || colIdx == null) {
                continue;
            }
            checked.add(new BingoLineJudge.Pos(rowIdx, colIdx));
        }

        var progress = BingoLineJudge.judge(board.getSize(), checked, BingoLineJudge.CompletionPolicy.oneLine());

        // 완성된 모든 라인 찾기
        List<BingoLine> completedLines = findCompletedLines(allCells, board.getSize());

        // 이미 보상받은 라인 제외
        List<BingoLine> newLines = completedLines.stream()
                .filter(line -> !lineRewardRepo.existsByBoardIdAndLine(board.getId(), line.getType(), line.getIndex()))
                .collect(Collectors.toList());

        // 포인트 지급 (새로 완성된 라인에 대해서만)
        int points = 0;
        for (BingoLine line : newLines) {
            // DB에 라인 보상 기록 저장 (중복 지급 방지)
            lineRewardRepo.save(line.toEntity(board.getId()));

            // 포인트 지급
            pointsPort.addPoints(board.getMemberId(), perLinePoint, "BINGO_LINE");
            recordPointHistory(board, perLinePoint, Point.Distinction.EARN);
            points += perLinePoint;
        }

        // 보드에 완성된 라인 수 업데이트
        long totalRewardedLines = lineRewardRepo.countByBoardId(board.getId());
        board.setCompletedLineCount((int) totalRewardedLines);

        // 보드 완전 완성 상태 업데이트 (포인트 지급 없이 상태만 변경)
        boolean wasBoardCompleted = Boolean.TRUE.equals(board.getCompleted());
        if (progress.isCompleted() && !wasBoardCompleted) {
            board.setCompleted(true);
        }

        return CheckCellWithUploadResult.builder()
                .boardId(board.getId())
                .cellId(cell.getId())
                .wasAlreadyChecked(wasAlreadyChecked)
                .newlyCompletedLineCount(newLines.size())
                .boardCompletedNow(progress.isCompleted())
                .pointsGranted(points)
                .uploadedPath("/uploads/" + saved.relativePath())
                .storedFileName(saved.storedFileName())
                .build();
    }

    private Long resolveExtendFilePathId(Long requested) {
        if (requested != null) {
            return requested;
        }
        return defaultExtendFilePathId;
    }

    @Override
    @Transactional
    public boolean deleteUploadedFile(Integer fileId, Long memberId) {
        // 파일 업로드 엔티티 조회
        BingoFileUploadEntity upload = uploadRepo.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("업로드된 파일을 찾을 수 없습니다."));

        // 연관된 셀 조회
        BingoCellEntity cell = upload.getCell();
        BingoBoardEntity board = cell.getBoard();

        // 본인의 보드인지 확인
        if (!board.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("본인 보드의 파일만 삭제할 수 있습니다.");
        }

        // DB에서 파일 업로드 기록 먼저 삭제
        uploadRepo.delete(upload);

        // 파일 시스템에서 파일 삭제 (DB 삭제 후 시도)
        String relativePath = upload.getPath();
        boolean fileDeleted = false;
        try {
            fileDeleted = fileStorage.delete(relativePath);
        } catch (Exception e) {
            // 파일 삭제 실패해도 DB는 이미 삭제됨
            System.err.println("Failed to delete file from storage: " + relativePath);
        }

        return fileDeleted;
    }

    @Override
    @Transactional
    public boolean cancelCellCheck(Integer boardId, Integer cellId, Long memberId) {
        // 셀 조회 (락 사용)
        BingoCellEntity cell = cellRepo.findByIdForUpdate(cellId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 셀입니다."));

        // 보드 조회 (락 사용)
        BingoBoardEntity board = boardRepo.findByIdForUpdate(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 보드입니다."));

        // 본인의 보드인지 확인
        if (!board.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("본인 보드만 수정할 수 있습니다.");
        }

        // 셀이 체크되어 있는지 확인
        if (!Boolean.TRUE.equals(cell.getChecked())) {
            throw new IllegalArgumentException("이미 체크 취소된 셀입니다.");
        }

        // 연관된 파일들 찾아서 삭제
        List<BingoFileUploadEntity> uploads = uploadRepo.findByBingoCellId(cellId);
        for (BingoFileUploadEntity upload : uploads) {
            // 파일 시스템에서 파일 삭제
            try {
                fileStorage.delete(upload.getPath());
            } catch (Exception e) {
                // 파일 삭제 실패해도 계속 진행
                System.err.println("Failed to delete file: " + upload.getPath());
            }
        }
        // DB에서 파일 업로드 기록 삭제
        uploadRepo.deleteAll(uploads);

        // 셀 체크 취소
        cell.setChecked(false);
        cell.setCheckedAt(null);

        // 라인 카운트 재계산
        List<BingoLineJudge.Pos> checked = new ArrayList<>();
        var allCells = cellRepo.findAllByBoardIdForUpdate(board.getId());
        for (BingoCellEntity c : allCells) {
            if (!Boolean.TRUE.equals(c.getChecked())) {
                continue;
            }
            Integer rowIdx = toZeroBased(c.getRow());
            Integer colIdx = toZeroBased(c.getCol());
            if (rowIdx == null || colIdx == null) {
                continue;
            }
            checked.add(new BingoLineJudge.Pos(rowIdx, colIdx));
        }

        var progress = BingoLineJudge.judge(board.getSize(), checked, BingoLineJudge.CompletionPolicy.oneLine());

        // 현재 완성된 라인 수 계산 (이미 보상받은 것은 그대로 유지)
        List<BingoLine> completedLines = findCompletedLines(allCells, board.getSize());
        long currentCompletedCount = completedLines.size();
        long rewardedCount = lineRewardRepo.countByBoardId(board.getId());

        // 보드의 완성 라인 수 업데이트 (보상받은 라인 수 유지)
        board.setCompletedLineCount((int) rewardedCount);

        // 보드 완성 상태 업데이트
        if (!progress.isCompleted() && Boolean.TRUE.equals(board.getCompleted())) {
            board.setCompleted(false);
        }

        return true;
    }

    private void recordPointHistory(BingoBoardEntity board, int amount, Point.Distinction distinction) {
        Point history = new Point();
        history.setMemberId(board.getMemberId());
        history.setPoint(amount);
        history.setDistinction(distinction);
        history.setBingoBoardId(board.getId());
        pointRepository.save(history);
    }

    private Integer toZeroBased(Integer value) {
        if (value == null) {
            return null;
        }
        if (value <= 0) {
            return value;
        }
        return value - 1;
    }

    /**
     * 빙고판에서 완성된 모든 라인(가로/세로/대각선)을 찾아 반환
     */
    private List<BingoLine> findCompletedLines(List<BingoCellEntity> cells, int size) {
        List<BingoLine> result = new ArrayList<>();

        // 2D 배열로 체크 상태 맵핑 (1-based index)
        boolean[][] checked = new boolean[size + 1][size + 1];
        for (BingoCellEntity cell : cells) {
            if (Boolean.TRUE.equals(cell.getChecked())) {
                checked[cell.getRow()][cell.getCol()] = true;
            }
        }

        // 가로줄 체크
        for (int r = 1; r <= size; r++) {
            boolean lineComplete = true;
            for (int c = 1; c <= size; c++) {
                if (!checked[r][c]) {
                    lineComplete = false;
                    break;
                }
            }
            if (lineComplete) {
                result.add(BingoLine.row(r - 1)); // 0-based index로 저장
            }
        }

        // 세로줄 체크
        for (int c = 1; c <= size; c++) {
            boolean lineComplete = true;
            for (int r = 1; r <= size; r++) {
                if (!checked[r][c]) {
                    lineComplete = false;
                    break;
                }
            }
            if (lineComplete) {
                result.add(BingoLine.col(c - 1)); // 0-based index로 저장
            }
        }

        // 대각선1 체크 (↘: 왼쪽 위 → 오른쪽 아래)
        boolean diag1Complete = true;
        for (int i = 1; i <= size; i++) {
            if (!checked[i][i]) {
                diag1Complete = false;
                break;
            }
        }
        if (diag1Complete) {
            result.add(BingoLine.diag1());
        }

        // 대각선2 체크 (↙: 오른쪽 위 → 왼쪽 아래)
        boolean diag2Complete = true;
        for (int i = 1; i <= size; i++) {
            if (!checked[i][size - i + 1]) {
                diag2Complete = false;
                break;
            }
        }
        if (diag2Complete) {
            result.add(BingoLine.diag2());
        }

        return result;
    }

}
