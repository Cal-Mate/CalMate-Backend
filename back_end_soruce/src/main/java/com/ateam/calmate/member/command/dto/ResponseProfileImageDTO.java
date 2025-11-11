package com.ateam.calmate.member.command.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseProfileImageDTO {
    Long cumId;
    String dirPath;
    String filePath;
    String urlPath;
    boolean successUpload;
    String exceptionMessage;
}
