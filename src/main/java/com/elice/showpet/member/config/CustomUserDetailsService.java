package com.elice.showpet.member.config;

import com.elice.showpet.member.persistence.MemberEntity;
import com.elice.showpet.member.persistence.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MemberEntity> optionalMember = memberRepository.findByLoginId(username);
        if (optionalMember.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        MemberEntity member = optionalMember.get();

        return User.builder()
                .username(member.getLoginId())
                .password(member.getLoginPw()) // 암호화된 비밀번호 사용
                .build();
    }
}
