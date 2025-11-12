package com.ateam.calmate.qna.command.service;

import com.ateam.calmate.qna.command.dto.*;
import com.ateam.calmate.qna.command.entity.Qna;
import com.ateam.calmate.qna.command.entity.QnaComment;
import com.ateam.calmate.qna.command.repository.QnaCommentRepository;
import com.ateam.calmate.qna.command.repository.QnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaCommandService {

    private final QnaRepository qnaRepository;
    private final QnaCommentRepository qnaCommentRepository;

    /* QnA */

    public Qna createQna(QnaCreateRequest req) {
        Qna qna = Qna.builder()
                .memberId(req.getMemberId())
                .title(req.getTitle())
                .contents(req.getContents())
                .createdAt(LocalDateTime.now())
                .build();
        return qnaRepository.save(qna);


    }

    public Qna updateQna(Long qnaId, QnaUpdateRequest req) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("QnA를 찾을 수 없습니다. id=" + qnaId));

        if (req.getTitle() != null) qna.setTitle(req.getTitle());
        if (req.getContents() != null) qna.setContents(req.getContents());

        return qna;
    }

    public void deleteQna(Long qnaId) {
        qnaRepository.deleteById(qnaId);
    }

    /* 댓글 */

    public QnaComment createComment(Long qnaId, QnaCommentCreateRequest req) {
        Qna qna = qnaRepository.findById(qnaId)
                .orElseThrow(() -> new IllegalArgumentException("QnA를 찾을 수 없습니다. id=" + qnaId));

        QnaComment comment = QnaComment.builder()
                .qna(qna)
                .memberId(req.getMemberId())
                .comment(req.getComment())
                .parentCommentId(req.getParentCommentId())
                .createdAt(LocalDateTime.now())
                .build();

        return qnaCommentRepository.save(comment);
    }

    public QnaComment updateComment(Long commentId, QnaCommentUpdateRequest req) {
        QnaComment comment = qnaCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. id=" + commentId));

        if (req.getComment() != null) {
            comment.setComment(req.getComment());
        }
        return comment;
    }

    public Long deleteComment(Long commentId) {
        QnaComment comment = qnaCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. id=" + commentId));
        Long qnaId = comment.getQna().getId();
        qnaCommentRepository.delete(comment);
        return qnaId;
    }
}
