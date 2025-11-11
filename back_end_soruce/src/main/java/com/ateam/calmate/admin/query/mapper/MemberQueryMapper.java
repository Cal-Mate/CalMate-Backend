package com.ateam.calmate.admin.query.mapper;

import com.ateam.calmate.admin.query.dto.MemberSituationDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberQueryMapper {
    MemberSituationDTO selectMemberSituation();
}
