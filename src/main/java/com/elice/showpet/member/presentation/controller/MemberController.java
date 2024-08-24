package com.elice.showpet.member.presentation.controller;


import com.elice.showpet.member.application.MemberService;
import com.elice.showpet.member.application.dto.MemberLoginRequest;
import com.elice.showpet.member.persistence.MemberEntity;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("member", new MemberEntity());
        return "register";
    }

    @PostMapping("/register")
    public String registerMember(@ModelAttribute MemberEntity member, Model model){
        memberService.register(member);
        model.addAttribute("message", "회원가입 완료, 로그인 해주세요.");
        return "redirect:/member/login";
    }
    @GetMapping("/login")
    public String showLoginForm(Model model, HttpSession session) {
        // 세션에서 메시지 가져오기
        String successMessage = (String) session.getAttribute("successMessage");
        String errorMessage = (String) session.getAttribute("errorMessage");

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage"); // 메시지 표시 후 제거
        }
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage"); // 메시지 표시 후 제거
        }

        model.addAttribute("member", new MemberEntity());
        return "login";
    }
    @PostMapping("/login")
    public String loginMember(@RequestBody MemberLoginRequest member, HttpSession session) {
        System.out.println(member);
        Optional<MemberEntity> loggedInMember = memberService.login(member.getLoginId(), member.getLoginPw());
        if (loggedInMember.isPresent()) {
            session.setAttribute("member", loggedInMember.get());
            session.setAttribute("successMessage", "로그인에 성공했습니다!"); // 성공 메시지 설정
            return "redirect:/member/profile";
        } else {
            session.setAttribute("errorMessage", "잘못된 ID, PW 입니다. 다시 시도해주세요."); // 실패 메시지 설정
            return "redirect:/member/login";
        }
    }

    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        MemberEntity member = (MemberEntity) session.getAttribute("member");
        if (member == null) {
            return "redirect:/member/login";
        }
        model.addAttribute("member", member);
        return "profile";
    }

    @GetMapping("/logout")
    public String logoutMember(HttpSession session) {
        session.invalidate();
        return "redirect:/member/login";
    }

}
