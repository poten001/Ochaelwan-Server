package com.example.TCC.service;

import com.example.TCC.domain.Member;
import com.example.TCC.dto.request.NicknameRequestDto;
import com.example.TCC.dto.response.MemberResponseDto;
import com.example.TCC.exception.UnAuthorizedException;
import com.example.TCC.kakao.jwt.AuthTokensGenerator;
import com.example.TCC.manager.member.MemberEditor;
import com.example.TCC.manager.member.MemberRetriever;
import com.example.TCC.manager.member.MemberSaver;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {

    private final MemberRetriever memberRetriever;
    private final MemberEditor memberEditor;
    private final MemberSaver memberSaver;
    private final AuthTokensGenerator authTokensGenerator;

    //현재 로그인한 사용자의 정보 가져오기
    public Member getCurrentUser(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnAuthorizedException("유효하지 않은 토큰입니다.");
        }

        String accessToken = authorizationHeader.substring(7); // "Bearer " 접두어 제거
        Long memberId = authTokensGenerator.extractMemberId(accessToken); // memberId 추출

        return memberRetriever.findById(memberId);
    }

    //회원 전체 조회
    public List<MemberResponseDto> members() {
        List<Member> members = memberRetriever.findAll();
        List<MemberResponseDto> dtos = new ArrayList<MemberResponseDto>();
        for (Member m : members) {
            MemberResponseDto dto = MemberResponseDto.createMemberDto(m);
            dtos.add(dto);
        }
        return dtos;
    }

    //로그인한 회원 정보 조회
    public MemberResponseDto member(Member member) {
        return MemberResponseDto.createMemberDto(member);
    }

    @Transactional
    //회원 정보 수정(닉네임 수정)
    public MemberResponseDto update(Member member, NicknameRequestDto dto) {
        memberEditor.updateNickname(member, dto); // 닉네임 수정
        Member updatedMember = memberSaver.save(member);
        return MemberResponseDto.createMemberDto(updatedMember);
    }
}
