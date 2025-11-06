package com.ateam.calmate.member.query.service;

import com.ateam.calmate.member.query.dto.RequestLoginwithAuthoritiesDTO;
import com.ateam.calmate.member.query.dto.BlackListDTO;

public interface MemberQueryService {
    BlackListDTO findBlakListByMemberId(Long id);

    RequestLoginwithAuthoritiesDTO findMemberWithAuthoriesByEmail(String email);
}
