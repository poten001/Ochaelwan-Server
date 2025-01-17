package com.example.TCC.domain;

import com.example.TCC.dto.request.NicknameRequestDto;
import com.example.TCC.kakao.OAuthProvider;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String nickname;

    @Column
    private String profileImg;

    @Column
    private OAuthProvider oAuthProvider;

    @Column
    private String refreshToken;

//    @Column
//    private String kakaoAccessToken;

    @Builder
    public Member(String email, String nickname, String profileUrl, OAuthProvider oAuthProvider, String refreshToken) {
        this.email = email;
        this.nickname = nickname;
        this.profileImg = profileUrl;
        this.oAuthProvider = oAuthProvider;
        this.refreshToken = refreshToken;
    }

    public void patch(NicknameRequestDto dto) {
        if(this.nickname != dto.getMemberName())
            this.nickname = dto.getMemberName();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void clearRefreshToken() {
        this.refreshToken = null;
    }
}
