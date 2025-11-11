package com.ateam.calmate.calendar.command.controller;

import com.ateam.calmate.calendar.command.dto.CalendarCreateRequest;
import com.ateam.calmate.calendar.command.dto.CalendarUpdateRequest;
import com.ateam.calmate.calendar.command.entity.CalendarEntity;
import com.ateam.calmate.calendar.command.service.CalendarCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarCommandController {

    private final CalendarCommandService calendarCommandService;

    /** 생성 */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CalendarCreateRequest req) {
        CalendarEntity created = calendarCommandService.create(req);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "캘린더가 생성되었습니다.",
                "data", created
        ));
    }

    /** 수정 */
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody CalendarUpdateRequest req
    ) {
        CalendarEntity updated = calendarCommandService.update(id, req);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "캘린더가 수정되었습니다.",
                "data", updated
        ));
    }

    /** 삭제 */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        calendarCommandService.delete(id);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "캘린더가 삭제되었습니다.",
                "id", id
        ));
    }
}
