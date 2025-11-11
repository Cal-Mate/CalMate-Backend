package com.ateam.calmate.admin.query.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResponseDashboardCurrentSituationDTO {
    private Long totalMembers;
    private Long activeMembers;
    private Long waitingReport;
    private Long waitingInquiries;
    private Long newMembers;
    private Long stopedMembers;
    private Long blockedMembers;
    private Long totalBoards;
    private Long tatalPoints;
}
