package com.ateam.calmate.community.query.service;

import com.ateam.calmate.community.command.entity.PostFile;
import com.ateam.calmate.community.command.repository.PostFileRepository;
import com.ateam.calmate.community.query.dto.PostDetailDTO;
import com.ateam.calmate.community.query.mapper.PostMapperDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostServiceDetail {
    private final PostMapperDetail postMapperDetail;
    private final PostFileRepository postFileRepository; // ✅ 추가

    public PostDetailDTO getPostDetail(int postId, Long memberId) {
        PostDetailDTO dto = postMapperDetail.findPostDetail(postId, memberId);

        // ✅ 이미지 리스트 채우기
        dto.setImages(
                postFileRepository.findAllByPostId(postId).stream()
                        .map(PostFile::getUrl) // 저장한 상대경로 (/img/community/xxx)
                        .collect(Collectors.toList())
        );

        return dto;
    }
}
