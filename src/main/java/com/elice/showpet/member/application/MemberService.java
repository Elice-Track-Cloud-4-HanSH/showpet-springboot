package com.elice.showpet.member.application;


import com.elice.showpet.member.persistence.MemberEntity;
import com.elice.showpet.member.persistence.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public MemberEntity register(MemberEntity member){
        member.setLoginPw(passwordEncoder.encode(member.getLoginPw()));
        return memberRepository.save(member);
    }

//    public Optional<MemberEntity> login(String loginId, String loginPw){
//        Optional<MemberEntity> member = memberRepository.findByLoginId(loginId);
//        if(member.isPresent() && passwordEncoder.matches(loginPw, member.get().getLoginPw())){
//            return member;
//        }
//        return Optional.empty();
//    }
    public Optional<MemberEntity> login(String loginId, String loginPw) {
        Optional<MemberEntity> member = memberRepository.findByLoginId(loginId);
        if (member.isPresent()) {
            boolean passwordMatches = passwordEncoder.matches(loginPw, member.get().getLoginPw());
            System.out.println("Password matches: " + passwordMatches); // 디버깅을 위한 로그
            if (passwordMatches) {
            return member;
            }
        }
        return Optional.empty();
}

}
