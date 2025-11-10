package com.ateam.calmate.qna.command.repository;

import com.ateam.calmate.qna.command.entity.Qna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnaRepository extends JpaRepository<Qna, Long> {
}
