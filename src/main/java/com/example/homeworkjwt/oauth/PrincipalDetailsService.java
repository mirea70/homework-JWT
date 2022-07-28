package com.example.homeworkjwt.oauth;

import com.example.homeworkjwt.model.Member;
import com.example.homeworkjwt.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member memberEntity = memberRepository.findByUsername(username);
        return new PrincipalDetails(memberEntity);
    }
}
