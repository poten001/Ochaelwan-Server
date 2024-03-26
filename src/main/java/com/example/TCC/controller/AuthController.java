package com.example.TCC.controller;

import com.example.TCC.domain.Member;
import com.example.TCC.dto.response.AccessTokenGetSuccess;
import com.example.TCC.kakao.KakaoApiClient;
import com.example.TCC.kakao.KakaoLoginParams;
import com.example.TCC.kakao.jwt.AuthTokens;
import com.example.TCC.service.MemberService;
import com.example.TCC.service.OAuthLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final OAuthLoginService oAuthLoginService;
    private final KakaoApiClient kakaoApiClient;
    private final MemberService memberService;

    //카카오 로그인
    @PostMapping("/login")
    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }

    //accessToken 재발급 (Bearer 없이 param으로)
    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenGetSuccess> refresh(@RequestParam("refreshToken") String refreshToken) {
        AccessTokenGetSuccess accessToken = oAuthLoginService.refreshToken(refreshToken);
        return ResponseEntity.ok(accessToken);
    }

    //로그아웃 (Bearer과 함께 header로)
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        Member member = memberService.getCurrentUser(authorizationHeader);

        oAuthLoginService.logout(member);
        return ResponseEntity.ok("로그아웃 완료");
    }
}
