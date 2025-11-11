package com.ateam.calmate.diary.command.repository;

import com.ateam.calmate.diary.command.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
}
