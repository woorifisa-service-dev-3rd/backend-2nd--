package dev.lawlesszone.global.config;

import dev.lawlesszone.domain.Member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Lazy
    @Autowired
    private MemberService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()  // CSRF 보호를 비활성화
                .authorizeRequests()
                .antMatchers("/**") .permitAll()
                .anyRequest().authenticated()  // 그 외의 모든 요청은 인증 필요
                .and()
                .formLogin((formLogin)->formLogin.loginPage("/member/login").defaultSuccessUrl("/").usernameParameter("email").passwordParameter("password").permitAll())
                .userDetailsService(userDetailsService);;
//                .logout((logout)->logout.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")).logoutSuccessUrl("/").invalidateHttpSession(true))// 로그인 폼 설정

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}