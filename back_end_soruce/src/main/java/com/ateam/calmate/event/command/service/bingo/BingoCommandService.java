package com.ateam.calmate.event.command.service.bingo;

import com.ateam.calmate.event.command.dto.bingo.CheckCellWithUploadCommand;
import com.ateam.calmate.event.command.dto.bingo.CheckCellWithUploadResult;

import java.io.InputStream;

public interface BingoCommandService {
    CheckCellWithUploadResult checkCellWithUpload(CheckCellWithUploadCommand cmd, InputStream fileStream);
    boolean deleteUploadedFile(Integer fileId, Long memberId);
    boolean cancelCellCheck(Integer boardId, Integer cellId, Long memberId);
}