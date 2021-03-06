package com.icia.member.controller;


import com.icia.member.dto.MemberDetailDTO;
import com.icia.member.dto.MemberLoginDTO;
import com.icia.member.dto.MemberSaveDTO;
import com.icia.member.entity.MemberEntity;
import com.icia.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.lang.reflect.Member;
import java.util.List;

import static com.icia.member.common.SessionConst.LOGIN_EMAIL;

@Controller
@RequestMapping("/member/*")
@RequiredArgsConstructor // final 이 붙은 필드로만 생성자를 만들어줌.
public class MemberController {
    private final MemberService ms;

    // 회원가입폼
    @GetMapping("save")
    public String saveForm(){
        return "member/save";
    }

    // 회원가입 처리
    @PostMapping("save")
    public String save(@ModelAttribute MemberSaveDTO memberSaveDTO) {
        Long memberId = ms.save(memberSaveDTO);
        return "member/login";
    }

    // 로그인폼
    @GetMapping("login")
    public String loginForm(){

        System.out.println("로그인화면 요청");
        return "member/login";
    }

    // 로그인처리
    @PostMapping("login")
    public String login(@ModelAttribute MemberLoginDTO memberLoginDTO, HttpSession session) {

        boolean loginResult = ms.login(memberLoginDTO);

        if(loginResult) {
//            session.setAttribute("loginEmail", memberLoginDTO.getMemberEmail());
            session.setAttribute(LOGIN_EMAIL, memberLoginDTO.getMemberEmail());
//            return "redirect:/member/findAll";
            return "member/mypage";

        } else {
            return "member/login";
        }

    }

    // 회원목록
    @GetMapping
    public String findAll(Model model) {
        List<MemberDetailDTO> memberList = ms.findAll();
        model.addAttribute("memberList", memberList);
        return "member/findAll";
    }

    // 상세조회
    // (/member/5)
    // @PathVariable: 경로상에 있는 변수를 가져올 때 사용
    @GetMapping("{memberId}")
    public String findById(@PathVariable("memberId") Long memberId, Model model) {
        System.out.println("memberId" + memberId);

        MemberDetailDTO member = ms.findById(memberId);
        model.addAttribute("member",member);
        return"member/findById";
    }

    //회원조회 ajax
    @PostMapping("{memberId}")
    public @ResponseBody MemberDetailDTO detail(@PathVariable("memberId") Long memberId) {
        MemberDetailDTO member = ms.findById(memberId);
        return member;
    }

    // 회원삭제 (/member/delete/5)
    @GetMapping ("delete/{memberId}")
    public String deleteById(@PathVariable("memberId") Long memberId) {
        ms.deleteById(memberId);
        return "redirect:/member/";
    }

    // 회원삭제 (/member/5)
    @DeleteMapping("{memberId}")
    public ResponseEntity deleteById2(@PathVariable Long memberId) {
        ms.deleteById(memberId);
        /*
            단순 화면 출력이 아닌 데이터를 리턴하고자 할 때 사용하는 리턴 방식
            ResponseEntity: 데이터 & http 상태코드(200, 400, 404, 405, 500 등)를 함께 리턴할 수 있음.
            @ReponseBody: 데이터를 리턴할 수 있음.
         */
        // 200 코드를 리턴
        return new ResponseEntity(HttpStatus.OK);
    }

    //업데이트
    @GetMapping("update")
    public String updateForm(Model model, HttpSession session){
        String memberEmail = (String) session.getAttribute(LOGIN_EMAIL);
        MemberDetailDTO member = ms.findByEmail(memberEmail);
        model.addAttribute("member", member);
        return "member/update";
    }

    //업데이트처리
    @PostMapping("update")
    public String update(@ModelAttribute MemberDetailDTO memberDetailDTO) {
        Long memberId = ms.update(memberDetailDTO);
        // 수정완료 후 해당회원의 상세페이지(findById.html) 출력
        return "redirect:/member/" + memberDetailDTO.getMemberId();
    }

    // 업데이트처리 (put)
    @PutMapping("{memberId}")
    // json 으로 데이터가 전달되면 @RequestBody로 받아줘야함. html에서 json
    public ResponseEntity update2(@RequestBody MemberDetailDTO memberDetailDTO) {
        Long memberId = ms.update(memberDetailDTO);
        return new ResponseEntity(HttpStatus.OK);
    }


}
