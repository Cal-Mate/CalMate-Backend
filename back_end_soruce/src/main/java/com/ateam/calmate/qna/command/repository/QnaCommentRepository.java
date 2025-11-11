package com.ateam.calmate.qna.command.repository;

import com.ateam.calmate.qna.command.entity.QnaComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaCommentRepository extends JpaRepository<QnaComment, Long> {
}
