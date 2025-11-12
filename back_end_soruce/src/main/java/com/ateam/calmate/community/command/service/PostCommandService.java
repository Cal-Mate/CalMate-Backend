package com.ateam.calmate.community.command.service;

import com.ateam.calmate.community.command.dto.PostCreateRequestDTO;
import com.ateam.calmate.community.command.dto.PostUpdateRequestDTO;
import com.ateam.calmate.community.command.entity.PostExtendFilePath;
import com.ateam.calmate.community.command.entity.Post;
import com.ateam.calmate.community.command.entity.PostFile;
import com.ateam.calmate.community.command.repository.CommunityPointRepository;
import com.ateam.calmate.community.command.repository.PostExtendFilePathRepository;
import com.ateam.calmate.community.command.repository.PostFileRepository;
import com.ateam.calmate.community.command.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostCommandService {

    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final CommunityPointRepository communityPointRepository;
    private final PostExtendFilePathRepository postextendFilePathRepository;

    @Transactional
    public void createPost(PostCreateRequestDTO dto) {

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .tagId(dto.getTagId())
                .memberId(dto.getMemberId())
                .visibility(0)
                .build();

        Post savedPost = postRepository.save(post);

        // 🔥 여러 개 저장
        if (dto.getImages() != null) {
            dto.getImages().forEach(img -> saveImage(img, savedPost.getId()));
        }

        // 포인트 +10 적립
        communityPointRepository.findById(dto.getMemberId())
                .ifPresent(member -> {
                    int currentPoint = member.getPoint() == null ? 0 : member.getPoint();
                    member.setPoint(currentPoint + 10);
                });
    }

    @Transactional
    public void updatePost(Integer postId, PostUpdateRequestDTO dto) {

        Post post = postRepository.findById(postId).orElseThrow();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setTagId(dto.getTagId());
        postRepository.save(post);

        // ✅ 1) 개별 삭제 기능
        if (dto.getDeleteImages() != null && !dto.getDeleteImages().isEmpty()) {
            dto.getDeleteImages().forEach(url -> {
                PostFile file = postFileRepository.findByUrl(url);
                if (file != null) {
                    if (file.getPath() != null) {
                        File disk = new File(file.getPath());
                        if (disk.exists()) disk.delete();
                    }
                    postFileRepository.delete(file);
                }
            });
        }

        // ✅ 2) 새 이미지 업로드
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            dto.getImages().forEach(img -> saveImage(img, postId));
        }
    }

    @Transactional
    public void deletePost(Integer postId) {

        List<PostFile> files = postFileRepository.findAllByPostId(postId);
        files.forEach(file -> {
            if (file.getPath() != null) {
                File diskFile = new File(file.getPath());
                if (diskFile.exists()) diskFile.delete();
            }
            postFileRepository.delete(file);
        });

        postRepository.deleteById(postId);
    }

    private void saveImage(MultipartFile image, Integer postId) {
        if (image == null || image.isEmpty()) return;

        try {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

            String folderPath = System.getProperty("user.dir") + "/img/community/";
            File folder = new File(folderPath);
            if (!folder.exists()) folder.mkdirs();

            String fullPath = folderPath + fileName;
            image.transferTo(new File(fullPath));

            // ✅ extend_file_path INSERT
            String accessUrl = "/img/community/" + fileName;
            PostExtendFilePath extend = postextendFilePathRepository.save(
                    PostExtendFilePath.builder()
                            .urlPath(accessUrl)
                            .build()
            );

            // ✅ post_file INSERT
            postFileRepository.save(PostFile.builder()
                    .name(image.getOriginalFilename())
                    .url(accessUrl)
                    .mimeType(image.getContentType())
                    .path(fullPath)
                    .state("ACTIVE")
                    .reName(fileName)
                    .postId(postId)
                    .extendFilePath(extend)
                    .build());

        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 실패", e);
        }
    }
}