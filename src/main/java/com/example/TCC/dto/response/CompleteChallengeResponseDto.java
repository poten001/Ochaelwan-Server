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
    private String takenTime;
    private String challengeTitle;
    private String challengeImg;

    public static CompleteChallengeResponseDto createCompleteChallengeDto(CompleteChall completeChall) {

        String formattedTakenTime = formatTakenTime(completeChall.getTakenTime());

        return new CompleteChallengeResponseDto(
                completeChall.getId(),
                completeChall.getNickname(),
                completeChall.getMemberProfileImg(),
                completeChall.getStartTime(),
                completeChall.getCompleteTime(),
                formattedTakenTime,
                completeChall.getChallengeTitle(),
                completeChall.getChallengeImg()
        );
    }

    //Duration 타입을 받아와서 조건에 맞는 문자열로 변환
    private static String formatTakenTime(Duration duration) {
        long seconds = duration.getSeconds();
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;

        if (hours > 0) {
            if (minutes == 0) {
                return String.format("%d시간 만에 완료", hours);
            } else {
                return String.format("%d시간 %d분 만에 완료", hours, minutes);
            }
        } else if (minutes > 0) {
            return String.format("%d분 만에 완료", minutes);
        } else {
            //1분 미만인 경우, 1분으로 처리
            return "1분 만에 완료";
        }

    }
}
