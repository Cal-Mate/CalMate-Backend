package com.ateam.calmate.community.query.mapper;

import com.ateam.calmate.community.query.dto.CommentResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<CommentResponseDTO> findByPostIdWithLike(@Param("postId") int postId,
                                                  @Param("memberId") long memberId);

    void insertComment(@Param("postId") int postId,
                       @Param("memberId") int memberId,
                       @Param("content") String content,
                       @Param("parentId") Integer parentId);

    // ✅ 댓글 수정
    void updateComment(@Param("commentId") int commentId,
                       @Param("content") String content);

    // ✅ 댓글 삭제
    void deleteComment(@Param("commentId") int commentId);

    // ✅ 댓글 작성자 ID 조회용
    Long findAuthorIdByCommentId(@Param("commentId") int commentId);
}