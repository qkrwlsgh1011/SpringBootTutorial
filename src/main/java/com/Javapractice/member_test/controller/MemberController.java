package com.Javapractice.member_test.controller;

import com.Javapractice.member_test.dto.MemberDTO;
import com.Javapractice.member_test.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    //생성자 주입
    private final MemberService memberService;

    //회원가입 페이지 출력 요청
    @GetMapping("/member_test/save")
    public String saveForm(){
        return "save";
    }

    @PostMapping("/member_test/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        //"memberEmail"에 담아온 값을 String memberEmail에 담는다.
        System.out.println("MemberController.save");
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO);  //service로 DTO객체로 보냄
        return "login";
    }

    @GetMapping("/member_test/login")
    public String loginForm() {
        return "login";
    }   // 로그인 페이지 가져오기

    @PostMapping("/member_test/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        // 1. 로그인이 제대로 됐는지 안됐는지
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // login 성공
            if (loginResult.getMemberEmail().equals("admin")){
                // 로그인한 사람이 admin일 경우 회원관리 페이지로 이동
                session.setAttribute("loginEmail", loginResult.getMemberEmail());
                return "admin";
            }
            else{
                // 일반회원 로그인일 경우
                session.setAttribute("loginEmail", loginResult.getMemberEmail());
                return "main";
            }
        }
        else{
            // login 실패
            return "login";
        }
    }

    @GetMapping("/member_test/")
    public String findAll(Model model){ // html로 가져갈 데이터 model 객체로
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "list";
    }

    @GetMapping("/member_test/{id}") //   이경로에 있는 값을 취하겠다.
    public String findById(@PathVariable("id") Long id, Model model) {    // 경로상에 있는 값을 담아온다. -> id와 model객체로 담아온다.
        System.out.println("MemberController.id");
        MemberDTO memberDTO = memberService.findById(id);   // 조회하는 데이터가 사용자 하나의 데이터기 때문에 DTO값으로
        model.addAttribute("member_test", memberDTO);    // 조회한 결과를 담아서 detail이라는 html로
        return "detail";
    }

    @GetMapping("/member_test/update")
    public String updateForm(HttpSession session, Model model) {
        String myEmail = (String) session.getAttribute("loginEmail");   // 세션으로 지금 로그인한 사람 정보 가져오기
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        model.addAttribute("updateMember", memberDTO);
        return "update";
    }

    @PostMapping("/member_test/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return "redirect:/member_test/" + memberDTO.getId();    // redirect를 사용해서 수정이 완료된 상세 페이지 조회 가능
    }

    @GetMapping("/member_test/delete/{id}")
    public String deleteById(@PathVariable("id") Long id) {
        memberService.deleteById(id);
        return "redirect:/member_test/";
    }

    @GetMapping("/member_test/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }
}
