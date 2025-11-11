package com.ateam.calmate.admin.query.controller;

import com.ateam.calmate.admin.query.dto.ResponseDashboardCurrentSituationDTO;
import com.ateam.calmate.admin.query.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardSituation(){
        ResponseDashboardCurrentSituationDTO responseData =
                adminService.getDashboardSituation();

        return null;
    }
}
