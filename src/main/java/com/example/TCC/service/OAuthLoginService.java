package com.example.TCC.service;

import com.example.TCC.domain.Member;
import com.example.TCC.dto.response.AccessTokenGetSuccess;
import com.example.TCC.exception.UnAuthorizedException;
import com.example.TCC.kakao.OAuthInfoResponse;
import com.example.TCC.kakao.OAuthLoginParams;
import com.example.TCC.kakao.RequestOAuthInfoService;
import com.example.TCC.kakao.jwt.AuthTokens;
import com.example.TCC.kakao.jwt.AuthTokensGenerator;
import com.example.TCC.kakao.jwt.JwtTokenProvider;
import com.example.TCC.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long memberId = findOrCreateMember(oAuthInfoResponse);
        AuthTokens authTokens = authTokensGenerator.generate(memberId);

        // refreshToken 업데이트
        memberRepository.findById(memberId).ifPresent(member -> {
            member.setRefreshToken(authTokens.getRefreshToken());
            memberRepository.save(member);
        });

        return authTokens;
    }

    private Long findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return memberRepository.findByEmail(oAuthInfoResponse.getEmail())
                .map(Member::getId)
                .orElseGet(() -> newMember(oAuthInfoResponse));
    }

    private Long newMember(OAuthInfoResponse oAuthInfoResponse) {
        Member member = Member.builder()
                .email(oAuthInfoResponse.getEmail())
                .nickname(oAuthInfoResponse.getNickname())
                .profileUrl(oAuthInfoResponse.getProfileImageUrl())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .build();

        return memberRepository.save(member).getId();
    }

    public AccessTokenGetSuccess refreshToken(final String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UnAuthorizedException("토큰이 유효하지 않습니다.");
        }

        Long userId = Long.valueOf(jwtTokenProvider.getSubject(refreshToken));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new UnAuthorizedException("멤버가 없습니다."));

        // DB에 저장된 refreshToken과 일치하는지 검사
        if (!refreshToken.equals(member.getRefreshToken())) {
            throw new UnAuthorizedException("토큰 정보가 일치하지 않습니다.");
        }

        // 새로운 accessToken 생성 (refreshToken은 유지)
        String newAccessToken = jwtTokenProvider.generateToken(userId.toString(), AuthTokensGenerator.ACCESS_TOKEN_EXPIRE_TIME);

        // 새로운 accessToken을 반환하는 DTO 객체 생성
        return AccessTokenGetSuccess.of(newAccessToken);
    }

    public void logout(Member member) {
            member.setRefreshToken(null); // 또는 적절한 무효화 방법 선택
            memberRepository.save(member);
    }
}
