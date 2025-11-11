package com.ateam.calmate.event.command.dto.bingo;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CheckCellWithUploadCommand {
    private Long memberId;
    private Integer boardId;
    private Integer cellId;
    private Long extendFilePathId;
    private String originalFilename;
    private String contentType;
    private long fileSize;
}