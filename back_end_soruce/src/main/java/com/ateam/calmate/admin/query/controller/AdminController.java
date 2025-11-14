package com.ateam.calmate.admin.query.controller;

import com.ateam.calmate.admin.query.dto.AdminDashBoardDTO;
import com.ateam.calmate.admin.query.dto.ResponseDashboardCurrentSituationDTO;
import com.ateam.calmate.admin.query.service.AdminService;
import com.ateam.calmate.common.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ResponseMessage> getDashboardSituation(){
        ResponseDashboardCurrentSituationDTO responseData =
                adminService.getDashboardSituation();

        AdminDashBoardDTO dashboard =  adminService.getDashboard();

        ResponseMessage responseMessage = new ResponseMessage();
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("responseData", responseData);
        responseMap.put("dashboard", dashboard);

        responseMessage.setHttpStatus(HttpStatus.OK.value());
        responseMessage.setResult(responseMap);

        return ResponseEntity.ok().body(responseMessage);
    }
}
