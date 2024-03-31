package com.example.TCC.service;

import com.example.TCC.domain.Member;
import com.example.TCC.dto.response.AccessTokenGetSuccess;
import com.example.TCC.exception.ConflictException;
import com.example.TCC.exception.UnAuthorizedException;
import com.example.TCC.kakao.*;
import com.example.TCC.kakao.jwt.AuthTokens;
import com.example.TCC.kakao.jwt.AuthTokensGenerator;
import com.example.TCC.kakao.jwt.JwtTokenProvider;
import com.example.TCC.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final MemberRepository memberRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthTokens login(OAuthLoginParams params) {
//        String accessToken = requestOAuthInfoService.getClients().get(params.oAuthProvider()).requestAccessToken(params);
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long memberId = findOrCreateMember(oAuthInfoResponse);
        AuthTokens authTokens = authTokensGenerator.generate(memberId);

        // accessToken 및 refreshToken 업데이트
        memberRepository.findById(memberId).ifPresent(member -> {
            member.setRefreshToken(authTokens.getRefreshToken());
            // 카카오 액세스 토큰을 Member 엔티티에 저장
//            member.setKakaoAccessToken(accessToken);
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

    @Transactional(readOnly = true)
    public AccessTokenGetSuccess refreshToken(final String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new UnAuthorizedException("토큰이 유효하지 않습니다.");
        }

        Long userId = Long.valueOf(jwtTokenProvider.extractSubject(refreshToken));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new UnAuthorizedException("멤버가 없습니다."));

        // DB에 저장된 refreshToken과 일치하는지 검사
        if (!refreshToken.equals(member.getRefreshToken())) {
            throw new UnAuthorizedException("토큰 정보가 일치하지 않습니다.");
        }

        // 새로운 accessToken 생성을 위한 로직 수정
        long now = (new Date()).getTime();
        Date accessTokenExpiredAt = new Date(now + 1000 * 60 * 30);
        String newAccessToken = jwtTokenProvider.generate(userId.toString(), accessTokenExpiredAt);

        // 새로운 accessToken을 반환하는 DTO 객체 생성
        return AccessTokenGetSuccess.of(newAccessToken);
    }

    @Transactional
    public void logout(Member member) {
            member.setRefreshToken(null); // 또는 적절한 무효화 방법 선택
            memberRepository.save(member);
    }

//    @Value("${oauth.kakao.url.api}")
//    private String apiUrl;
//
//    @Transactional
////회원탈퇴
//    public void delete(Member member) {
//        String accessToken = member.getKakaoAccessToken();
//
//        sendDeleteRequest(accessToken);
//
//        deleteMember(member);
//    }
//    private void deleteMember(Member member) {
//        memberRepository.delete(member);
//    }
//
//    private void sendDeleteRequest(String accessToken) {
//        try {
//            RestTemplate restTemplate = new RestTemplate();
//            String url = apiUrl + "/v1/user/unlink";
//
//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.add("Authorization", "Bearer " + accessToken);
//
//            HttpEntity<?> request = new HttpEntity<>(httpHeaders);
//
//            restTemplate.postForObject(url, request, String.class);
//
//            // 응답 상태 코드 검사
////            if (response.getStatusCode() == HttpStatus.OK) {
////                // 성공 처리: 성공적으로 요청이 처리된 경우의 로직을 여기에 작성
////                System.out.println("회원 탈퇴 요청 성공");
////            } else {
////                // 오류 처리: 요청이 실패했거나 오류가 발생한 경우의 로직을 여기에 작성
////                throw new ConflictException("회원 탈퇴 요청 실패: " + response.getStatusCode());
////            }
//        } catch (HttpClientErrorException e) {
//            // 4xx 클라이언트 오류
//            throw new ConflictException("클라이언트 오류: " + e.getResponseBodyAsString());
//        } catch (HttpServerErrorException e) {
//            // 5xx 서버 오류
//            throw new ConflictException("서버 오류: " + e.getStatusCode());
//        } catch (Exception e) {
//            // 그 외 예외
//            throw new ConflictException("알 수 없는 오류 발생");
//        }
//    }

}
