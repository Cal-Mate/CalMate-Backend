package com.ateam.calmate.dietManagement.command.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "food_fileupload")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodFileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String type;

    @Column(name = "re_name", length = 255, nullable = false)
    private String reName;

    @Column(length = 255, nullable = false)
    private String path;

    @Column(name = "thumb_path", length = 255, nullable = false)
    private String thumbPath;

    @Column(name = "upload_order", nullable = false)
    private Integer uploadOrder;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extend_file_path_id", nullable = false)
    private FoodExtendFilePath extendFilePath;

    @PrePersist
    void onCreate() {
        if (createAt == null) {
            createAt = LocalDateTime.now();
        }
    }
}
