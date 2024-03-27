package com.example.TCC.dto.response;

import com.example.TCC.domain.CompleteChall;
import com.example.TCC.domain.TryChall;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CompleteChallengeResponseDto {
    private Long id;
    private String memberName;
    private String memberProfile;
    private LocalDateTime startTime;
    private LocalDateTime completeTime;
    private Duration takenTime;
    private String challengeTitle;
    private String challengeImg;

    public static CompleteChallengeResponseDto createCompleteChallengeDto(CompleteChall completeChall) {

        return new CompleteChallengeResponseDto(
                completeChall.getId(),
                completeChall.getNickname(),
                completeChall.getMemberProfileImg(),
                completeChall.getStartTime(),
                completeChall.getCompleteTime(),
                completeChall.getTakenTime(),
                completeChall.getChallengeTitle(),
                completeChall.getChallengeImg()
        );
    }
}
