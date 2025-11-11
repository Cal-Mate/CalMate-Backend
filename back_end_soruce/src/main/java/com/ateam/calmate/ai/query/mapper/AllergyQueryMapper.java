package com.ateam.calmate.ai.query.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface AllergyQueryMapper {
    List<String> findMemberAllergyNames(BigInteger memberId);
}
