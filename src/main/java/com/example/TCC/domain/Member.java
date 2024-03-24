package com.example.TCC.domain;

import com.example.TCC.kakao.OAuthProvider;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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

    @Builder
    public Member(String email, String nickname, String profileUrl, OAuthProvider oAuthProvider) {
        this.email = email;
        this.nickname = nickname;
        this.profileImg = profileUrl;
        this.oAuthProvider = oAuthProvider;
    }
}
