package com.example.TCC.controller;

import com.example.TCC.domain.Member;
import com.example.TCC.dto.response.TryChallengeResponseDto;
import com.example.TCC.service.MemberService;
import com.example.TCC.service.TryChallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class TryChallController {

    private final MemberService memberService;
    private final TryChallService tryChallService;

    // 챌린지 도전
    @PostMapping("/try")
    public ResponseEntity<?> tryChallenge(@RequestHeader("Authorization") String authorizationHeader) {
        Member member = memberService.getCurrentUser(authorizationHeader);
        tryChallService.tryChallenge(member);
        return ResponseEntity.status(HttpStatus.OK).body("챌린지에 도전하셨습니다.");
    }

    // 챌린지 도전항목 조회
    @GetMapping("/try/check")
    public ResponseEntity<TryChallengeResponseDto> tryCheck(@RequestHeader("Authorization") String authorizationHeader) {
        Member member = memberService.getCurrentUser(authorizationHeader);
        TryChallengeResponseDto tryDto = tryChallService.tryCheck(member);
        return ResponseEntity.status(HttpStatus.OK).body(tryDto);
    }
}
