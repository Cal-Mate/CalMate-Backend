package com.ateam.calmate.diary.command.service;

import com.ateam.calmate.diary.command.dto.*;
import com.ateam.calmate.diary.command.entity.*;
import com.ateam.calmate.diary.command.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryCommandService {

    private final DiaryRepository diaryRepository;

    public Diary createDiary(DiaryCreateRequest req) {
        Diary diary = Diary.builder()
                .day(req.getDay())
                .weight(req.getWeight())
                .mood(req.getMood())
                .condition(req.getCondition())
                .memo(req.getMemo())
                .memberId(req.getMemberId())
                .build();

        if (req.getFiles() != null) {
            req.getFiles().forEach(f -> {
                DiaryFile file = DiaryFile.builder()
                        .mime(f.getMime())
                        .path(f.getPath())
                        .state(f.getState())
                        .originalFile(f.getOriginalFile())
                        .rename(f.getRename())
                        .extendFilePathId(f.getExtendFilePathId())
                        .build();
                diary.addFile(file);
            });
        }

        return diaryRepository.save(diary);
    }

    public Diary updateDiary(Integer diaryId, DiaryUpdateRequest req) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("일기를 찾을 수 없습니다."));

        if (req.getWeight() != null) diary.setWeight(req.getWeight());
        if (req.getMood() != null) diary.setMood(req.getMood());
        if (req.getCondition() != null) diary.setCondition(req.getCondition());
        if (req.getMemo() != null) diary.setMemo(req.getMemo());

        if (req.getFiles() != null) {
            diary.clearFiles();
            req.getFiles().forEach(f -> {
                DiaryFile file = DiaryFile.builder()
                        .mime(f.getMime())
                        .path(f.getPath())
                        .state(f.getState())
                        .originalFile(f.getOriginalFile())
                        .rename(f.getRename())
                        .extendFilePathId(f.getExtendFilePathId())
                        .build();
                diary.addFile(file);
            });
        }

        return diary;
    }

    public void deleteDiary(Integer diaryId) {
        diaryRepository.deleteById(diaryId);
    }
}
