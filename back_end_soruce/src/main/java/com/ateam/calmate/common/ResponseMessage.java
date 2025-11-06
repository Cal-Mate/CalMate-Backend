package com.ateam.calmate.common;

import java.util.Map;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseMessage {
    private int httpStatus;             //응답 상태 코드 값
    private String message;             //응답 메세지
    private Map<String, Object> result; //응답데이터
}
