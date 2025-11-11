package com.ateam.calmate.admin.query.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MemberSituationDTO {
    private Long newMembers;
    private Long stopedMembers;
    private Long blockedMembers;
    private Long totalMembers;
    private Long activeMembers;
}
