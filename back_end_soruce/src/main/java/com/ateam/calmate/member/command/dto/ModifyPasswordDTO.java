package com.ateam.calmate.member.command.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ModifyPasswordDTO {
    Long id;
    String newPassword;
    String oldPassword;
}
