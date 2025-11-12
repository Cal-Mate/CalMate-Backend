package com.ateam.calmate.member.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_rank")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          // 1~5 (등급 레벨)

    private String name;      // "스타터", "챌린저" ...

    @Column(name = "badge_count")
    private Integer badgeCount;  // 해당 레벨 최소 필요 뱃지 수

}
