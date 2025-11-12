package com.ateam.calmate.member.command.repository;

import com.ateam.calmate.member.command.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByJti(String jti);           // jti로 찾기
    List<RefreshTokenEntity> findAllByMemberId(Long userId);        // 사용자 전부 폐기용
}
