package com.elice.showpet.member.filter;

import com.elice.showpet.member.persistence.MemberEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class CustomSecurityFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // parse Cookie test key
        Optional<Cookie> testCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("test"))
                .findAny();

        if (testCookie.isPresent()) {
            String testUserName = testCookie.get().getValue();
            UserDetails userDetails = userDetailsService.loadUserByUsername(testUserName);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if ((username != null && password != null)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // 비밀번호 확인
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new IllegalAccessError("비밀번호가 일치하지 않습니다.");
            }

            // 인증 객체 생성 및 등록
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Cookie testCook = new Cookie("test", userDetails.getUsername());
            testCook.setPath("/");
            testCook.setMaxAge(60 * 60 * 24);
            testCook.setSecure(true);
            testCook.setHttpOnly(true);
            response.addCookie(testCook);
        }

        filterChain.doFilter(request, response);
    }

}