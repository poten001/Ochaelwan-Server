package com.example.TCC.service;

import com.example.TCC.domain.Member;
import com.example.TCC.dto.response.MemberResponseDto;
import com.example.TCC.exception.NotFoundException;
import com.example.TCC.exception.UnAuthorizedException;
import com.example.TCC.kakao.jwt.AuthTokensGenerator;
import com.example.TCC.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;

    //현재 로그인한 사용자의 정보 가져오기
    public Member getCurrentUser(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnAuthorizedException("유효하지 않은 토큰입니다.");
        }

        String accessToken = authorizationHeader.substring(7); // "Bearer " 접두어 제거
        Long memberId = authTokensGenerator.extractMemberId(accessToken); // memberId 추출

        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    }

    //회원 전체 조회
    public List<MemberResponseDto> members() {
        List<Member> members = memberRepository.findAll();
        List<MemberResponseDto> dtos = new ArrayList<MemberResponseDto>();
        for (Member m : members) {
            MemberResponseDto dto = MemberResponseDto.createMemberDto(m);
            dtos.add(dto);
        }
        return dtos;
    }
}
