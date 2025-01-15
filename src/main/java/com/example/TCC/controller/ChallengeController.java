package com.example.TCC.controller;

import com.example.TCC.domain.Member;
import com.example.TCC.dto.request.DrawChallengeRequestDto;
import com.example.TCC.dto.response.DrawChallengeResponseDto;
import com.example.TCC.service.ChallengeService;
import com.example.TCC.service.CompleteChallService;
import com.example.TCC.service.MemberService;
import com.example.TCC.service.TryChallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChallengeController {

    private final MemberService memberService;
    private final ChallengeService challengeService;
    private final TryChallService tryChallService;
    private final CompleteChallService completeChallService;

    // 챌린지 뽑기
    @PostMapping("/challenge/draw")
    public ResponseEntity<DrawChallengeResponseDto> draw(@RequestBody DrawChallengeRequestDto dto, @RequestHeader("Authorization") String authorizationHeader) {
        Member member = memberService.getCurrentUser(authorizationHeader);
        DrawChallengeResponseDto drawDto = challengeService.draw(dto, member);
        return ResponseEntity.status(HttpStatus.OK).body(drawDto);
    }
}