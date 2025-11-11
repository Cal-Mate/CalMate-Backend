package com.ateam.calmate.community.command.repository;

import com.ateam.calmate.community.command.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostFileRepository extends JpaRepository<PostFile, Integer> {
    Optional<PostFile> findByPostId(Integer postId); // 🔥 단일 이미지 조회용
    List<PostFile> findAllByPostId(Integer postId);  // ✅ 여러 이미지 조회 / 삭제 / 업데이트용
    PostFile findByUrl(String url); // ✅ 추가 (URL 로 파일 조회)

}