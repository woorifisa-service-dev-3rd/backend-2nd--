package dev.lawlesszone.domain.Member.service;

import dev.lawlesszone.domain.Member.dto.*;
import dev.lawlesszone.domain.Member.entity.Member;
import dev.lawlesszone.domain.Member.repository.MemberRepository;
import dev.lawlesszone.global.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
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
            Member newMember = Member.builder().email(email).password(endCodedPassword).nickName(nickName).authorities("USER").build();
            return SignupResponseDTO.fromEntity(memberRepository.save(newMember));
        } else {
            return null;
        }
    }

    public TokenDTO login(LoginRequestDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return jwtTokenProvider.generateToken(authentication);
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

    private CustomUserDetail createUserDetails(Member member) {
        Collection<? extends GrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + member.getAuthorities()));
        return CustomUserDetail.builder()
                .Id(member.getId())
                .email(member.getEmail())
                .authorities(authorities)
                .build();
    }
}
