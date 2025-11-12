package com.ateam.calmate.community.command.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter
public class PostUpdateRequestDTO {
    private String title;
    private String content;
    private Integer tagId;
    private Boolean deleteImage;
    //private MultipartFile image;

    // 🔥 단일 -> 여러 개로 변경
    private List<MultipartFile> images;
    private List<String> deleteImages;  // ✅ 개별 삭제 요청 이미지들

}