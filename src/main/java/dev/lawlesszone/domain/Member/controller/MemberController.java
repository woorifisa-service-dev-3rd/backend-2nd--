package dev.lawlesszone.domain.Member.controller;

import dev.lawlesszone.domain.Member.dto.MemberInfoDTO;
import dev.lawlesszone.domain.Member.dto.SignupRequestDTO;
import dev.lawlesszone.domain.Member.dto.SignupResponseDTO;
import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.Member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;




@Slf4j
@RequestMapping("/member")
@RestController
@RequiredArgsConstructor
//@Slf4j
public class MemberController {
    private final MemberService memberService;

//    @GetMapping("/signup")
//    public String signupForm() {
//        return "member/signup";
//    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signUp(@Validated SignupRequestDTO signupRequestDTO) {
        SignupResponseDTO result = memberService.signUp(signupRequestDTO);
        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/login")
//    public String loginForm() {
//        return "member/login";
//    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail")
    public ResponseEntity<MemberInfoDTO> user(Authentication authentication, Model model) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return new ResponseEntity<>(memberService.findByEmail(email), HttpStatus.OK);
    }
}
