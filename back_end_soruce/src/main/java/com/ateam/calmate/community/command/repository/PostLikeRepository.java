package com.ateam.calmate.community.command.repository;

import com.ateam.calmate.community.command.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByPostIdAndMemberId(Integer postId, Long memberId);

    Optional<PostLike> findFirstByPostIdAndMemberId(Integer postId, Long memberId);

    void deleteByPostIdAndMemberId(Integer postId, Long memberId);

    long countByPostId(Integer postId);
}