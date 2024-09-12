package dev.lawlesszone.global.filter;

import dev.lawlesszone.global.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = jwtTokenProvider.resolveToken(request);
        String path = request.getRequestURI();
        if (path.equals("/member/login") || path.equals("/member/signup") || path.equals("/swagger-ui/index.html")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 토큰 유효성 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (token == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인되지 않았습니다.");
            return;
        } else {
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "토큰이 만료되었습니다.");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
