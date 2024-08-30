package com.elice.showpet.member.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public CustomAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher){
        super(requiresAuthenticationRequestMatcher);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String id = request.getParameter("username");
        String credentials = request.getParameter("password");

        log.info("username : " + id);
        log.info("password : " + credentials);

        Authentication authResult = new UsernamePasswordAuthenticationToken(id, credentials);

        return getAuthenticationManager().authenticate(authResult);
    }
}
