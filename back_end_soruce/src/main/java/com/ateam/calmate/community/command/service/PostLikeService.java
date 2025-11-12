package com.ateam.calmate.community.command.service;

import com.ateam.calmate.community.command.entity.PostLike;
import com.ateam.calmate.community.command.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    /**
     * true면 '좋아요 추가', false면 '좋아요 취소' 상태로 토글된 뒤 총 개수 리턴
     */
    @Transactional
    public long togglePostLike(Integer postId, Long memberId) {
        boolean liked = postLikeRepository.existsByPostIdAndMemberId(postId, memberId);

        if (liked) {
            postLikeRepository.deleteByPostIdAndMemberId(postId, memberId);
        } else {
            // 유니크 제약으로 인한 동시성 예외 대비
            try {
                postLikeRepository.save(
                        PostLike.builder()
                                .postId(postId)
                                .memberId(memberId)
                                .build()
                );
            } catch (DataIntegrityViolationException e) {
                // 거의 동시에 눌러 중복 insert가 들어온 경우: 무시하고 진행
            }
        }
        return postLikeRepository.countByPostId(postId);
    }
}