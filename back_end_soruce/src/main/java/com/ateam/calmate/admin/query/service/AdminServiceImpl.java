package com.ateam.calmate.admin.query.service;

import com.ateam.calmate.admin.query.dto.AdminDashBoardDTO;
import com.ateam.calmate.admin.query.dto.MemberSituationDTO;
import com.ateam.calmate.admin.query.dto.ResponseDashboardCurrentSituationDTO;
import com.ateam.calmate.admin.query.mapper.MemberQueryMapper;
import com.ateam.calmate.member.command.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final MemberQueryMapper memberQueryMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminServiceImpl(
            MemberQueryMapper memberQueryMapper
            , ModelMapper modelMapper
    ) {
        this.memberQueryMapper = memberQueryMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseDashboardCurrentSituationDTO getDashboardSituation() {

        MemberSituationDTO memberSituationDTO = memberQueryMapper.selectMemberSituation();
        ResponseDashboardCurrentSituationDTO responseData
                = modelMapper.map(memberSituationDTO, ResponseDashboardCurrentSituationDTO.class);
        return responseData;
    }

    @Override
    public AdminDashBoardDTO getDashboard() {
        AdminDashBoardDTO dashboard = memberQueryMapper.selectAdminDashBoard();

        return dashboard;
    }
}
