package com.example.TCC.dto.response;

import com.example.TCC.domain.TryChall;

import java.time.LocalDateTime;

public class TryChallengeResponseDto {
    private Long id;
    private String memberName;
    private String memberProfile;
    private LocalDateTime startTime;
    private LocalDateTime expireTime;
    private String challengeTitle;
    private String challengeImg;

    public static DrawChallengeResponseDto createDrawChallengeDto(TryChall tryChall) {

        return new DrawChallengeResponseDto(
                tryChall.getId(),
                tryChall.getMember().getNickname(),
                tryChall.getMember().getProfileImg(),
                tryChall.getStartTime(),
                tryChall.getExpireTime(),
                tryChall.getChallenge().getTitle(),
                tryChall.getChallenge().getCategory().getCategoryImg()
        );
    }
}
