package com.example.TCC.manager.member;

import com.example.TCC.domain.Member;
import com.example.TCC.dto.request.NicknameRequestDto;
import org.springframework.stereotype.Component;

@Component
public class MemberEditor {

    public void updateNickname(final Member member, final NicknameRequestDto dto) {
        member.patch(dto); // 닉네임 수정
    }

    public void updateRefreshToken(final Member member, final String refreshToken) {
        member.updateRefreshToken(refreshToken);
    }

    public void clearRefreshToken(final Member member) {
        member.clearRefreshToken();
    }
}
