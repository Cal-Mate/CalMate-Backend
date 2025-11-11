package com.ateam.calmate.diary.command.repository;

import com.ateam.calmate.diary.command.entity.DiaryFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryFileRepository extends JpaRepository<DiaryFile, Integer> {

    List<DiaryFile> findByDiary_Id(Integer diaryId);

    void deleteByIdInAndDiary_Id(List<Integer> ids, Integer diaryId);
}
