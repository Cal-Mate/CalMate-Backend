package com.ateam.calmate.report.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReportFileDto {
    private Integer id;
    private String name;
    private String type;
    private String reName;
    private String path;
    private String thumbPath;
    private Integer uploadOrder;
    private String fileUrl;
    private String thumbUrl;
}
