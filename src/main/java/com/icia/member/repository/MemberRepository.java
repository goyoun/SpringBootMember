package com.icia.member.repository;

import com.icia.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // 이메일을 조건으로 회원조회 (select * from member_table where member_email=?)
    /*
        메서드 리턴타입 : MemberEntity
        메서드 이름 : findByMemberEmail
        메서드 매개변수 : String memberEmail
     */
    MemberEntity findByMemberEmail(String memberEmail);
}
