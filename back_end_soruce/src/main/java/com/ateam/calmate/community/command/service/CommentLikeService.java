package com.ateam.calmate.community.command.service;

import com.ateam.calmate.community.command.entity.CommentLike;
import com.ateam.calmate.community.command.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public long toggleCommentLike(Integer commentId, Long memberId) {

        boolean liked = commentLikeRepository.existsByPostCommentIdAndMemberId(commentId, memberId);

        if (liked) {
            commentLikeRepository.deleteByPostCommentIdAndMemberId(commentId, memberId);
        } else {
            try {
                commentLikeRepository.save(
                        CommentLike.builder()
                                .postCommentId(commentId)
                                .memberId(memberId)
                                .build()
                );
            } catch (DataIntegrityViolationException ignored) {}
        }

        return commentLikeRepository.countByPostCommentId(commentId);
    }
}