package com.ateam.calmate.event.command.service.bingo;

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
import com.ateam.calmate.event.query.dto.bingo.BingoLineJudge;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BingoCommandServiceImpl implements BingoCommandService {

    private final BingoBoardCommandRepository boardRepo;
    private final BingoCellCommandRepository cellRepo;
    private final BingoFileUploadCommandRepository uploadRepo;
    private final FileStoragePort fileStorage;
    private final PointsPort pointsPort;

    @Value("${bingo.points.perLine:50}")
    private int perLinePoint;

    @Value("${bingo.points.boardBonus:200}")
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

        // 라인 판정
        List<BingoLineJudge.Pos> checked = new ArrayList<>();
        var allCells = cellRepo.findAllByBoardId(board.getId());
        for (BingoCellEntity c : allCells) {
            if (Boolean.TRUE.equals(c.getChecked())) {
                checked.add(new BingoLineJudge.Pos(c.getRow(), c.getCol()));
            }
        }

        var progress = BingoLineJudge.judge(board.getSize(), checked, BingoLineJudge.CompletionPolicy.oneLine());

        // 포인트 지급
        int points = 0;
        if (progress.getCompletedLineCount() > 0) {
            pointsPort.addPoints(board.getMemberId(), perLinePoint, "BINGO_LINE");
            points += perLinePoint;
        }
        if (progress.isCompleted()) {
            pointsPort.addPoints(board.getMemberId(), boardBonusPoint, "BINGO_COMPLETE");
            points += boardBonusPoint;
        }

        return CheckCellWithUploadResult.builder()
                .boardId(board.getId())
                .cellId(cell.getId())
                .wasAlreadyChecked(wasAlreadyChecked)
                .newlyCompletedLineCount(progress.getCompletedLineCount())
                .boardCompletedNow(progress.isCompleted())
                .pointsGranted(points)
                .uploadedPath(saved.relativePath())
                .storedFileName(saved.storedFileName())
                .build();
    }

    private Long resolveExtendFilePathId(Long requested) {
        if (requested != null) {
            return requested;
        }
        return defaultExtendFilePathId;
    }
}
