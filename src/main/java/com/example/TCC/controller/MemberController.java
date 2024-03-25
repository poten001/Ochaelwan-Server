package com.example.TCC.controller;

import com.example.TCC.domain.Member;
import com.example.TCC.dto.request.NicknameRequestDto;
import com.example.TCC.dto.response.MemberResponseDto;
import com.example.TCC.exception.NotFoundException;
import com.example.TCC.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    //회원 전체 조회
    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> showAll() {
        List<MemberResponseDto> dtos = memberService.members();
        if (dtos == null) {
            throw new NotFoundException("멤버가 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    //로그인한 사용자 정보 조회
    @GetMapping("/member")
    public ResponseEntity<MemberResponseDto> show(@RequestHeader("Authorization") String authorizationHeader) {
        //로그인한 사용자가 있는지 확인
        Member member = memberService.getCurrentUser(authorizationHeader);

        MemberResponseDto dto = memberService.member(member);
        if (dto == null) {
            throw new NotFoundException("멤버가 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    //회원 정보 수정(닉네임 수정)
    @PatchMapping("/member")
    public ResponseEntity<MemberResponseDto> update(@RequestBody NicknameRequestDto nickname, @RequestHeader("Authorization") String authorizationHeader) {
        //로그인한 사용자가 있는지 확인
        Member member = memberService.getCurrentUser(authorizationHeader);

        MemberResponseDto dto = memberService.update(member, nickname);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
