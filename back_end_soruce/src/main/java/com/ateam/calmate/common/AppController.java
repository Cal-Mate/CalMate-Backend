package com.ateam.calmate.common;

import jakarta.servlet.ServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;
import java.util.List;

@RestController
@Slf4j
public class AppController {
    //서버 상태 체크
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck(ServletRequest request) {
         Enumeration<String> d = request.getAttributeNames();
         while (d.hasMoreElements()) {
             String header = d.nextElement();
             System.out.println("=================");
             System.out.println(header);
             System.out.println(request.getAttribute(header));
             System.out.println("=================");
         }
        return ResponseEntity.ok("OK");
    }
}
