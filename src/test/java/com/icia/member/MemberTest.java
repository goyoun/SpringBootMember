package com.icia.member;

import com.icia.member.dto.MemberLoginDTO;
import com.icia.member.dto.MemberSaveDTO;
import com.icia.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

    //테스트 코드에는 @SpringBootTest를 붙인다
    @SpringBootTest
    public class MemberTest {
        /*
            테스트코드 주 목적
            코드가 잘 작동 되는지 먼저 확인 해보는 곳

            MemberServiceImpl.save() 메서드가 잘 동작하는지를 테스트

            회원가입 테스트
            save.html 에서 회원정보 입력 후 가입 클릭
            DB 확인

         */
        @Autowired
        private MemberService ms;

        @Test
        @DisplayName("회원가입 테스트")
        public void memberSaveTest() {
            MemberSaveDTO memberSaveDTO = new MemberSaveDTO();
            memberSaveDTO.setMemberEmail("1");
            memberSaveDTO.setMemberPassword("1");
            memberSaveDTO.setMemberName("1");
            ms.save(memberSaveDTO);
        }


        @Test
        @Transactional // 테스트 시작할때 새로운 트랜잭션 시작
        @Rollback
        @DisplayName("로그인테스트")
        public void loginTest() {
        /*
            1. 테스트용 회원가입 (MemberSaveDTO)를이용
            2. 로그인용 객체 생성(MemberLoginDTO)를 이용
            1.2. 수행할 때 동일한 이메일, 패스워드를 사용하도록 함.
            3. 로그인 수행
            4. 로그인 결과가 true냐 false냐 확인
         */
            //given
            final String testMemberEmail = "로그인테스트이메일";
            //파이널 선언하면 다른변수 기입 못함
            final String testMemberPassword = "로그인테스트비밀번호";
            final String testMemberName = "로그인 테스트 이름";
            final String wrongMemberEmail = "이건true면안돼";
            // 1. 회원가입
            MemberSaveDTO memberSaveDTO = new MemberSaveDTO(testMemberEmail, testMemberPassword, testMemberName);
            ms.save(memberSaveDTO);
            //when
            // 2. 로그인
            MemberLoginDTO memberLoginDTO = new MemberLoginDTO(testMemberEmail, testMemberPassword);
            boolean loginResult = ms.login(memberLoginDTO);
            //then
            assertThat(loginResult).isEqualTo(true);
        }

        @Test
        @DisplayName("회원데이터생성")
        public void newMember() {
            IntStream.rangeClosed(1, 15). forEach(i -> {
                ms.save(new MemberSaveDTO("email"+i,"pw"+i,"name"+i));
            });
        }

        @Test
        @DisplayName("회원조회")
        public void findByIdTest() {

        }


    }
