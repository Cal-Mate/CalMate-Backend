package com.ateam.calmate.community.query.service;

import com.ateam.calmate.community.query.dto.CommentResponseDTO;
import com.ateam.calmate.community.query.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    public List<CommentResponseDTO> getComments(int postId, long memberId) {

        List<CommentResponseDTO> flat = commentMapper.findByPostIdWithLike(postId, memberId);

        Map<Integer, CommentResponseDTO> byId = new LinkedHashMap<>();
        List<CommentResponseDTO> roots = new ArrayList<>();

        for (CommentResponseDTO c : flat) {
            byId.put(c.getId(), c);
            c.setReplies(new ArrayList<>());
        }

        for (CommentResponseDTO c : flat) {
            if (c.getParentId() == null) {
                roots.add(c);
            } else {
                CommentResponseDTO parent = byId.get(c.getParentId());
                if (parent != null) parent.getReplies().add(c);
                else roots.add(c);
            }
        }
        return roots;
    }

    public void addComment(int postId, int memberId, String content, Integer parentId) {
        commentMapper.insertComment(postId, memberId, content, parentId);
    }


    // ✅ 댓글 수정
    public void updateComment(int commentId, String content) {
        commentMapper.updateComment(commentId, content);
    }

    // ✅ 댓글 삭제
    public void deleteComment(int commentId) {
        commentMapper.deleteComment(commentId);
    }

}