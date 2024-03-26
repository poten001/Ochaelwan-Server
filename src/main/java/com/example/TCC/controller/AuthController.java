package com.example.TCC.controller;

import com.example.TCC.kakao.KakaoApiClient;
import com.example.TCC.kakao.KakaoLoginParams;
import com.example.TCC.kakao.jwt.AuthTokens;
import com.example.TCC.service.OAuthLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final OAuthLoginService oAuthLoginService;
    private final KakaoApiClient kakaoApiClient;

    //카카오 로그인
    @PostMapping("/login")
    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }
}
