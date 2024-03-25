package com.example.TCC.dto.response;

import com.example.TCC.domain.Member;
import com.example.TCC.kakao.OAuthProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberResponseDto {
    private String email;
    private String nickname;
    private String profileUrl;
    private OAuthProvider oAuthProvider;

    public static MemberResponseDto createMemberDto(Member member) {
        return new MemberResponseDto(
                member.getEmail(),
                member.getNickname(),
                member.getProfileImg(),
                member.getOAuthProvider()
        );
    }
}
