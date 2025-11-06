package com.ateam.calmate.member.command.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CheckPasswordDTO {
    Long id;
    String password;
}
