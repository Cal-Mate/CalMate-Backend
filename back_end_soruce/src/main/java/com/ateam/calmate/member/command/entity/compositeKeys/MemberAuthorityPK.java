package com.ateam.calmate.member.command.entity.compositeKeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

/* 설명.
 *  Embeddable 타입의 하나의 값의 묶음이자 불변 객체로 다루는 타입이다.
 *  (불변 객체: setter가 없고 필드값에 변화가 생기면 새로운 객체를 생성해야 하는 객체)
 *  그리고 (equals/hashCode)를 오버라이딩 해야 한다.
 *  VO와 유사하며 @EmbaddedID로 복합키로 표현하고자 할 때 쓰기도 한다.
 * */

@Embeddable
public class MemberAuthorityPK {
    @Column(name = "member_id")
    private Long memberId;
    @Column(name = "authories_id")
    private Long authId;

    public MemberAuthorityPK() {
    }

    public MemberAuthorityPK(Long memberNo, Long memberId) {
        this.memberId = memberNo;
        authId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MemberAuthorityPK memberPK = (MemberAuthorityPK) o;
        return memberId == memberPK.memberId && Objects.equals(authId, memberPK.authId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, authId);
    }
}
