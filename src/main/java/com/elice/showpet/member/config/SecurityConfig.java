package com.elice.showpet.member.config;

import com.elice.showpet.member.filter.CustomAuthenticationFilter;
import com.elice.showpet.member.filter.CustomAuthenticationManager;
import com.elice.showpet.member.filter.CustomAuthenticationProvider;
import com.elice.showpet.member.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.lang.reflect.Member;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private MemberRepository memberRepository;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterAfter(customAuthenticationFilter(http), CsrfFilter.class)
                .authenticationManager(authenticationManager(http))
                .authenticationProvider(authenticationProvider()) // Custom AuthenticationProvider 설정
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/member/register", "/member/login/error", "/h2-console/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/member/login")
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/member/login?logout")
                                .permitAll()
                );
        return http.build();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(
                new AntPathRequestMatcher("/member/login", HttpMethod.POST.name())
        );
        filter.setAuthenticationManager(authenticationManager(http));
        filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/member/profile"));
        filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/member/login/error"));
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService(), passwordEncoder());
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomUserDetailsService(memberRepository);
    }


}
