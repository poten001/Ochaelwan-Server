package com.example.TCC.kakao;

public interface OAuthInfoResponse {
    String getEmail();
    String getNickname();

    String getProfileImageUrl();
    OAuthProvider getOAuthProvider();
}
