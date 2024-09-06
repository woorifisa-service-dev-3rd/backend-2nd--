package dev.lawlesszone.domain.Member.controller;

import dev.lawlesszone.domain.Member.dto.LoginRequestDTO;
import dev.lawlesszone.domain.Member.dto.SignupRequestDTO;
import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.Member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;



@Slf4j
@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
//@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupForm() {
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signUp(@Validated SignupRequestDTO signupRequestDTO) {
        try {
            Member member = memberService.signUp(signupRequestDTO);
            if (member == null) {
                return "error";
            }
            return "member/login";
        } catch (Exception e) {
            return "error";
        }
    }

    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }

}
