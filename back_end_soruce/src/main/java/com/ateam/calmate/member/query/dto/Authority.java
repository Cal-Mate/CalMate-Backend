package com.ateam.calmate.member.query.dto;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Authority {
    private Long cumId;
    private Long authId;
    private String authName;
    private String authDescribe;
}
