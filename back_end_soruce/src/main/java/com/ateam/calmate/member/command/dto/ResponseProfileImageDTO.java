package com.ateam.calmate.member.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseProfileImageDTO {
    private Long memberId;
    private String mimeType;
    private String filePath;
    private String state;
    private String originalFileName;
    private String reFileName;
    private Long extendFilePathId;
    private String urlPath;
    private boolean successUpload;
    private String exceptionMessage;
}
