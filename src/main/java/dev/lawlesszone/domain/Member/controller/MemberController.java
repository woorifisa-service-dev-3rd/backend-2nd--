package dev.lawlesszone.domain.Member.controller;

import dev.lawlesszone.domain.Member.dto.*;
import dev.lawlesszone.domain.Member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@RequestMapping("/member")
@RestController
@RequiredArgsConstructor
//@Slf4j
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signUp(@Validated SignupRequestDTO signupRequestDTO) {
        SignupResponseDTO result = memberService.signUp(signupRequestDTO);
        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        TokenDTO tokenDTO = memberService.login(loginRequestDTO);
        Cookie jwtCookie = new Cookie("accessToken", tokenDTO.getAccessToken());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(60*60*24*30);
        jwtCookie.setPath("/");
        jwtCookie.setSecure(false);
        response.addCookie(jwtCookie);
        return new ResponseEntity<>(memberService.login(loginRequestDTO),HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail")
    public ResponseEntity<MemberInfoDTO> user(Authentication authentication, Model model) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        return new ResponseEntity<>(memberService.findByEmail(email), HttpStatus.OK);
    }
}
