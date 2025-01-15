package com.example.TCC.controller;

import com.example.TCC.domain.Member;
import com.example.TCC.dto.response.CompleteChallengeResponseDto;
import com.example.TCC.exception.NotFoundException;
import com.example.TCC.service.CompleteChallService;
import com.example.TCC.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class CompleteChallController {

    private final MemberService memberService;
    private final CompleteChallService completeChallService;

    // 챌린지 완료
    @PostMapping("/complete")
    public ResponseEntity<CompleteChallengeResponseDto> complete(@RequestHeader("Authorization") String authorizationHeader) {
        Member member = memberService.getCurrentUser(authorizationHeader);
        CompleteChallengeResponseDto completeDto = completeChallService.complete(member);
        return ResponseEntity.status(HttpStatus.OK).body(completeDto);
    }

    // 완료한 챌린지 전체 조회
    @GetMapping("/completed")
    public ResponseEntity<List<CompleteChallengeResponseDto>> showAll(@RequestHeader("Authorization") String authorizationHeader) {
        Member member = memberService.getCurrentUser(authorizationHeader);
        List<CompleteChallengeResponseDto> dtos = completeChallService.showAll(member);

        if (dtos == null) {
            throw new NotFoundException("완료한 챌린지가 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    // 완료한 챌린지 상세 조회
    @GetMapping("/{challengeId}")
    public ResponseEntity<CompleteChallengeResponseDto> show(@PathVariable Long challengeId, @RequestHeader("Authorization") String authorizationHeader) {
        Member member = memberService.getCurrentUser(authorizationHeader);
        CompleteChallengeResponseDto dto = completeChallService.show(challengeId, member);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }
}
