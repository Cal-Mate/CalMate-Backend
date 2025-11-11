package com.ateam.calmate.event.command.dto.bingo;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CheckCellWithUploadResult {
    private Integer boardId;
    private Integer cellId;
    private boolean wasAlreadyChecked;
    private int newlyCompletedLineCount;
    private boolean boardCompletedNow;
    private int pointsGranted;
    private String uploadedPath;
    private String storedFileName;
}