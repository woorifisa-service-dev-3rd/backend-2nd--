package dev.lawlesszone.global.provider;

import dev.lawlesszone.domain.Member.dto.CustomUserDetail;
import dev.lawlesszone.domain.Member.dto.MemberInfoDTO;
import dev.lawlesszone.domain.Member.dto.TokenDTO;
import dev.lawlesszone.domain.Member.service.MemberService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@PropertySource("classpath:application-local.yml")
@Component
public class JwtTokenProvider {

    private final MemberService memberService;
    private final SecretKey key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, @Lazy MemberService memberService) {
        this.memberService = memberService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDTO generateToken(Authentication authentication) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        MemberInfoDTO member = memberService.findByEmail(email);
        Long id = member.getId();

        Date accessTokenExpire = Date.from(ZonedDateTime.now().plusMonths(1).toInstant());
        System.out.println(accessTokenExpire);
        String accessToken = Jwts.builder()
                .subject(authentication.getName())
                .claim("id", id)
                .claim("email", email)
                .claim("auth", authorities)
                .expiration(accessTokenExpire)
                .signWith(key, Jwts.SIG.HS256)
                .compact();

        return TokenDTO.fromString(accessToken);
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        CustomUserDetail userDetail = CustomUserDetail.builder()
                .Id((Long) claims.get("id"))
                .email((String) claims.get("email"))
                .authorities(authorities)
                .build();

        return new UsernamePasswordAuthenticationToken(userDetail, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String resolveToken(HttpServletRequest request) {    if (request.getCookies() != null) {
        for (Cookie cookie : request.getCookies()) {
            if ("accessToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
    }
        return null;
    }
}
