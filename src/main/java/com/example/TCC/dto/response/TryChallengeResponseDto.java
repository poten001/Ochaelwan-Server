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
public class TryChallengeResponseDto {
    private String memberName;
    private String memberProfile;
    private LocalDateTime startTime;
    private LocalDateTime expireTime;
    private String challengeTitle;
    private String challengeImg;

    public static TryChallengeResponseDto createTryChallengeDto(TryChall tryChall) {

        return new TryChallengeResponseDto(
                tryChall.getNickname(),
                tryChall.getMember().getProfileImg(),
                tryChall.getStartTime(),
                tryChall.getExpireTime(),
                tryChall.getChallenge().getTitle(),
                tryChall.getChallenge().getCategory().getCategoryImg()
        );
    }
}
