package com.ateam.calmate.diary.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "diary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDate day; // DB가 DATETIME 이면 @Column(columnDefinition="datetime") 써도 됨

    private Integer weight;

    @Column(length = 50)
    private String mood;

    @Column(name = "`condition`")
    private String condition;

    private String memo;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DiaryFile> files = new ArrayList<>();

    public void addFile(DiaryFile file) {
        files.add(file);
        file.setDiary(this);
    }

    public void clearFiles() {
        for (DiaryFile f : files) f.setDiary(null);
        files.clear();
    }
}
