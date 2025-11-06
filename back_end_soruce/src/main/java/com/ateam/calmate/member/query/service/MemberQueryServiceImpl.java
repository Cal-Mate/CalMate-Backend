package com.ateam.calmate.member.query.service;

import com.ateam.calmate.member.query.dto.RequestLoginwithAuthoritiesDTO;
import com.ateam.calmate.member.query.dto.BlackListDTO;
import com.ateam.calmate.member.query.mapper.MemberQueryRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberQueryServiceImpl implements MemberQueryService{
    private final MemberQueryRepository memberQueryRepository;

    public MemberQueryServiceImpl(MemberQueryRepository memberQueryRepository) {
        this.memberQueryRepository = memberQueryRepository;
    }

    @Override
    public BlackListDTO findBlakListByMemberId(Long id) {
        return memberQueryRepository.selectBlackListByMember(id);
    }

    @Override
    public RequestLoginwithAuthoritiesDTO findMemberWithAuthoriesByEmail(String email) {
        return memberQueryRepository.selectByIdWithAuthorite(email);
    }

}
