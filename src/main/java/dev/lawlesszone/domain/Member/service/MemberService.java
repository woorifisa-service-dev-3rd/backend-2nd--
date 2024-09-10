package dev.lawlesszone.domain.Member.service;

import ch.qos.logback.core.CoreConstants;
import dev.lawlesszone.domain.Member.dto.*;
import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.Member.repository.MemberRepository;
import dev.lawlesszone.domain.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    public SignupResponseDTO signUp(SignupRequestDTO signupRequestDTO) {
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
            return SignupResponseDTO.fromEntity(memberRepository.save(newMember));
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
        return MemberInfoDTO.from(memberRepository.findByEmail(email).orElseThrow());

    }
}
