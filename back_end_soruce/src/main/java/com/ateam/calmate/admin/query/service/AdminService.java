package com.ateam.calmate.admin.query.service;

import com.ateam.calmate.admin.query.dto.AdminDashBoardDTO;
import com.ateam.calmate.admin.query.dto.ResponseDashboardCurrentSituationDTO;

public interface AdminService {
    ResponseDashboardCurrentSituationDTO getDashboardSituation();

    AdminDashBoardDTO getDashboard();
}
