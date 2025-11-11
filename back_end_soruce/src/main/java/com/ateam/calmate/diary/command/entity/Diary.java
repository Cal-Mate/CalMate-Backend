package com.ateam.calmate.diary.command.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private LocalDate day;

    private Integer weight;

    @Column(length = 50)
    private String mood;

    @Column(name = "`condition`")
    private String condition;

    private String memo;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default                           // ✅ builder() 써도 기본값 유지
    private List<DiaryFile> files = new ArrayList<>();

    public void addFile(DiaryFile file) {
        if (this.files == null) {             // ✅ NPE 안전장치
            this.files = new ArrayList<>();
        }
        this.files.add(file);
        file.setDiary(this);
    }

    public void clearFiles() {
        if (this.files != null) {
            for (DiaryFile f : this.files) {
                f.setDiary(null);
            }
            this.files.clear();
        }
    }
}
