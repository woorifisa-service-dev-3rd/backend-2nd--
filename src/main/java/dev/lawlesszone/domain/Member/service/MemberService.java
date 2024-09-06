package dev.lawlesszone.domain.Member.service;

import ch.qos.logback.core.CoreConstants;
import dev.lawlesszone.domain.Member.dto.LoginRequestDTO;
import dev.lawlesszone.domain.Member.dto.LoginResponseDTO;
import dev.lawlesszone.domain.Member.dto.SignupRequestDTO;
import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    public Member signUp(SignupRequestDTO signupRequestDTO) {
        String email = signupRequestDTO.getEmail();
        String nickName = signupRequestDTO.getNickName();
        if (nickName == null || nickName.isEmpty()) {
            nickName = email;
        }
        String password = signupRequestDTO.getPassword();
        String confirmPassword = signupRequestDTO.getConfirmPassword();
        if (confirmPassword.equals(password)) {
            String endCodedPassword = passwordEncoder.encode(password);
            Member newMember = Member.builder().email(email).password(endCodedPassword).nickName(nickName).build();
            return memberRepository.save(newMember);
        } else {
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        optionalMember.orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        Member member = optionalMember.get();
        return new org.springframework.security.core.userdetails.User(member.getEmail(), member.getPassword(), new ArrayList<>());
    }

//    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws Exception {
//        String inputEmail = loginRequestDTO.getEmail();
//        String inputPassword = loginRequestDTO.getPassword();
//        System.out.println(inputEmail);
//        System.out.println(inputPassword);
//        Optional<Member> optionalMember = memberRepository.findByEmail(inputEmail);
//        optionalMember.orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
//        Member member = optionalMember.get();
//        if (passwordEncoder.matches(inputPassword, member.getPassword())) {
//            return LoginResponseDTO.createLoginResponseDTO(member);
//        } else {
//            throw new Exception("비밀번호가 일치하지 않습니다.");
//        }
//    }
}
