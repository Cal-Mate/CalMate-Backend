package com.ateam.calmate.event.command.controller;

import com.ateam.calmate.event.command.dto.gacha.GachaDrawRequest;
import com.ateam.calmate.event.command.dto.gacha.GachaDrawResult;
import com.ateam.calmate.event.command.service.gacha.GachaCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GachaStompController {

    private final GachaCommandService gachaService;

    @MessageMapping("/gacha/draw")
    @SendToUser("/queue/self") // (선택) 본인에게만 개별 응답도 가능
    public GachaDrawResult handleDraw(GachaDrawRequest req) {
        // draw() 내부에서 /topic/gacha/{eventId}로 브로드캐스트됨
        return gachaService.draw(req.getEventId(), req.getMemberId(), req.getCellId());
    }
}