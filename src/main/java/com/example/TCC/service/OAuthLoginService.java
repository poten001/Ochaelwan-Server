package com.example.TCC.service;

import com.example.TCC.domain.Member;
import com.example.TCC.dto.response.AccessTokenGetSuccess;
import com.example.TCC.exception.ConflictException;
import com.example.TCC.exception.UnAuthorizedException;
import com.example.TCC.kakao.*;
import com.example.TCC.kakao.jwt.AuthTokens;
import com.example.TCC.kakao.jwt.AuthTokensGenerator;
import com.example.TCC.kakao.jwt.JwtTokenProvider;
import com.example.TCC.manager.member.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final MemberRetriever memberRetriever;
    private final MemberSaver memberSaver;
    private final MemberEditor memberEditor;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long memberId = findOrCreateMember(oAuthInfoResponse);

        AuthTokens authTokens = authTokensGenerator.generate(memberId);

        // AccessToken 및 RefreshToken 업데이트
        Member member = memberRetriever.findById(memberId);
        memberEditor.updateRefreshToken(member, authTokens.getRefreshToken());
        memberSaver.save(member);

        return authTokens;
    }

    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return memberRetriever.findByEmail(oAuthInfoResponse.getEmail())
                .map(Member::getId)
                .orElseGet(() -> createNewMember(oAuthInfoResponse));
    }

    private Long createNewMember(OAuthInfoResponse oAuthInfoResponse) {
        Member member = Member.builder()
                .email(oAuthInfoResponse.getEmail())
                .nickname(oAuthInfoResponse.getNickname())
                .profileUrl(oAuthInfoResponse.getProfileImageUrl())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();

        return memberSaver.save(member).getId();
    }

    @Transactional(readOnly = true)
    public AccessTokenGetSuccess refreshToken(final String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UnAuthorizedException("토큰이 유효하지 않습니다.");
        }

        Long memberId = Long.valueOf(jwtTokenProvider.extractSubject(refreshToken));
        Member member = memberRetriever.findById(memberId);

        if (!refreshToken.equals(member.getRefreshToken())) {
            throw new UnAuthorizedException("토큰 정보가 일치하지 않습니다.");
        }

        // 새로운 AccessToken 생성
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + 1000 * 60 * 30);
        String newAccessToken = jwtTokenProvider.generate(memberId.toString(), accessTokenExpiredAt);

        return AccessTokenGetSuccess.of(newAccessToken);
    }

    @Transactional
    public void logout(Member member) {
        memberEditor.clearRefreshToken(member);
        memberSaver.save(member);
    }
}
