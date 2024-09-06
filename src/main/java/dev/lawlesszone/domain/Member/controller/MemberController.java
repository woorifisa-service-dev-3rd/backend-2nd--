package dev.lawlesszone.domain.Member.controller;

import dev.lawlesszone.domain.Member.dto.MemberInfoDTO;
import dev.lawlesszone.domain.Member.dto.SignupRequestDTO;
import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.Member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail")
    public String user(Authentication authentication, Model model) {

        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        MemberInfoDTO member = memberService.findByEmail(email);
        model.addAttribute("member",member);
        return "memebr/userDetail";
    }
}
