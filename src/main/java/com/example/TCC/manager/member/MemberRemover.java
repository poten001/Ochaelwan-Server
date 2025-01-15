package com.example.TCC.manager.member;

import com.example.TCC.domain.Member;
import com.example.TCC.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberRemover {

    private final MemberRepository memberRepository;

    public void delete(final Member member) {
        memberRepository.delete(member);
    }
}
