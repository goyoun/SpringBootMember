package com.icia.member.service;

import com.icia.member.dto.MemberDetailDTO;
import com.icia.member.dto.MemberLoginDTO;
import com.icia.member.dto.MemberSaveDTO;
import com.icia.member.entity.MemberEntity;
import com.icia.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository mr;

    // 회원가입
    @Override
    public Long save(MemberSaveDTO memberSaveDTO) {
        // JpaRepository는 무조건 Entity만 받음.

        // MemberSaveDTO -> MemberEntity 로 변환
        // memberSaveDTO를 전달하고 받는다
        MemberEntity memberEntity = MemberEntity.saveMember(memberSaveDTO);
//        두줄을 밑에 리턴한줄로 가능
//        Long memberId = mr.save(memberEntity).getId();
//        return memberId;
        System.out.println("MemberServiceImpl.save");
        return mr.save(memberEntity).getId();
    }

    // 로그인
    @Override
    public boolean login(MemberLoginDTO memberLoginDTO) {
        // 1. 사용자가 입력한 이메일을 조건으로 DB에서 조회 (select * from member_table where member_email=?)
        MemberEntity memberEntity = mr.findByMemberEmail(memberLoginDTO.getMemberEmail());
        // 2. 비밀번호 일치여부 확인

            if (memberEntity != null) {
                if (memberEntity.getMemberPassword().equals(memberLoginDTO.getMemberPassword())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

    // 회원 전체 조회
    @Override
    public List<MemberDetailDTO> findAll() {
        List<MemberEntity> memberEntityList = mr.findAll();
        List<MemberDetailDTO> memberList = new ArrayList<>();
        for (MemberEntity m: memberEntityList){
            memberList.add(MemberDetailDTO.toMemberDetailDTO(m));
        }
        return memberList;
    }

    // 회원 개인 조회회
    @Override
   public MemberDetailDTO findById(Long memberId) {
         /*
            1. MemberRepository로 부터 해당 회원의 정보를 MemberEntity로 가져옴.
            2. MemberEntity를 MemberDetailDTO로 바꿔서 컨트롤러로 리턴.
         */

        // 1.
        // Optional = null 방지
        Optional<MemberEntity> memberEntityOptional = mr.findById(memberId);
        // .get() 은 옵셔널안에 Entity를 꺼냄
        MemberEntity memberEntity = memberEntityOptional.get();
        // 2.
        MemberDetailDTO memberDetailDTO = MemberDetailDTO.toMemberDetailDTO(memberEntity);
        System.out.println("memberDetailDTO.toString() = " + memberDetailDTO.toString());
        return memberDetailDTO;
        // 위의 4줄을 밑에 한줄로도 가능하다.
        //return MemberDetailDTO.toMemberDetailDTO(mr.findById(memberId).get());

    }

    // 삭제
    @Override
    public void deleteById(Long memberId) {
        mr.deleteById(memberId);
    }

    // 업데이트
    @Override
    public MemberDetailDTO findByEmail(String memberEmail) {
        MemberEntity memberEntity = mr.findByMemberEmail(memberEmail);
        MemberDetailDTO memberDetailDTO = MemberDetailDTO.toMemberDetailDTO(memberEntity);
        return memberDetailDTO;
    }

    // 업데이트 후
    @Override
    public Long update(MemberDetailDTO memberDetailDTO) {
        // JPA는 update 처리시 save 메서드 호출
        // MemberDetailDTO -> MemberEntity 로 변경
        MemberEntity memberEntity = MemberEntity.toUpdateMember(memberDetailDTO);
        Long memberId = mr.save(memberEntity).getId();

        return memberId;
    }

}


