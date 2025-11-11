package com.ateam.calmate.admin.query.service;

import com.ateam.calmate.admin.query.dto.MemberSituationDTO;
import com.ateam.calmate.admin.query.dto.ResponseDashboardCurrentSituationDTO;
import com.ateam.calmate.admin.query.mapper.MemberQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final MemberQueryMapper memberQueryMapper;

    @Autowired
    public AdminServiceImpl(MemberQueryMapper memberQueryMapper) {
        this.memberQueryMapper = memberQueryMapper;
    }

    @Override
    public ResponseDashboardCurrentSituationDTO getDashboardSituation() {

        MemberSituationDTO memberSituationDTO = memberQueryMapper.selectMemberSituation();
        return null;
    }
}
