package com.example.TCC.dto.response;

import com.example.TCC.domain.TryChall;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DrawChallengeResponseDto {
    private String memberName;
    private String memberProfile;
    private LocalDateTime startTime;
    private LocalDateTime expireTime;
    private String challengeTitle;
    private String challengeImg;

    public static DrawChallengeResponseDto createDrawChallengeDto(TryChall tryChall) {

        return new DrawChallengeResponseDto(
                tryChall.getMember().getNickname(),
                tryChall.getMember().getProfileImg(),
                tryChall.getStartTime(),
                tryChall.getExpireTime(),
                tryChall.getChallenge().getTitle(),
                tryChall.getChallenge().getCategory().getCategoryImg()
        );
    }
}
