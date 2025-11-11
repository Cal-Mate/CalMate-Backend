package com.ateam.calmate.exerciseRecords.command.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercise")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String type;

    private String category;

    private Integer min;

    @Column(name = "burned_kcal")
    private Integer burnedKcal;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ExerciseFileUpload> files = new ArrayList<>();

    @PrePersist
    void onCreate() {
        if (createAt == null) {
            createAt = LocalDateTime.now();
        }
    }

    // 연관관계 편의 메서드
    public void addFile(ExerciseFileUpload file) {
        files.add(file);
        file.setExercise(this);
    }

    public void clearFiles() {
        for (ExerciseFileUpload f : files) {
            f.setExercise(null);
        }
        files.clear();
    }
}
