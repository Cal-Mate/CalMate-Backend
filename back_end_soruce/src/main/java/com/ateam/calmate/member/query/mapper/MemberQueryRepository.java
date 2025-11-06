package com.ateam.calmate.member.query.mapper;

import com.ateam.calmate.member.query.dto.RequestLoginwithAuthoritiesDTO;
import com.ateam.calmate.member.query.dto.BlackListDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberQueryRepository {
    BlackListDTO selectBlackListByMember(Long id);


    RequestLoginwithAuthoritiesDTO selectByIdWithAuthorite(String email);
}
