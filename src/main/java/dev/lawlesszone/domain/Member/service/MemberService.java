package dev.lawlesszone.domain.Member.service;

import dev.lawlesszone.domain.Member.dto.MemberInfoDTO;
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
    public MemberInfoDTO findByEmail(String email) {
        return MemberInfoDTO.createUserInfoDTO(memberRepository.findByEmail(email).orElseThrow());

    }
}
