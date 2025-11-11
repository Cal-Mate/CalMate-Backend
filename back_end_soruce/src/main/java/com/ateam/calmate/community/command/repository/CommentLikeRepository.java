package com.ateam.calmate.community.command.repository;

import com.ateam.calmate.community.command.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByPostCommentIdAndMemberId(Integer postCommentId, Long memberId);

    Optional<CommentLike> findFirstByPostCommentIdAndMemberId(Integer postCommentId, Long memberId);

    void deleteByPostCommentIdAndMemberId(Integer postCommentId, Long memberId);

    long countByPostCommentId(Integer postCommentId);
}