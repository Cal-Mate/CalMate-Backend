package com.ateam.calmate.member.command.entity;

import com.ateam.calmate.member.command.entity.compositeKeys.MemberAuthorityPK;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member_authority")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberAuthority {


    @EmbeddedId
    private MemberAuthorityPK memberAuthorityPK;
//    @Id
//    @Column(name = "member_id", nullable = false)
//    private Long memberId;
//
//    @Id
//    @Column(name = "authories_id")
//    private Long authId;

}
