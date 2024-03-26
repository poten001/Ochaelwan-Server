package com.example.TCC.dto.response;

public record AccessTokenGetSuccess(
        String accessToken
) {
    public static AccessTokenGetSuccess of(
            final String accessToken
    ) {
        return new AccessTokenGetSuccess(accessToken);
    }
}