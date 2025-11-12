package com.ateam.calmate.community.command.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter
public class PostCreateRequestDTO {

    private String title;
    private String content;
    private Integer tagId;
    private Long  memberId;   // 로그인 연동 전 → 1로 고정 가능
    //private MultipartFile image; // ✅ 이미지 포함

    // 🔥 단일 -> 여러 개로 변경
    private List<MultipartFile> images;
}