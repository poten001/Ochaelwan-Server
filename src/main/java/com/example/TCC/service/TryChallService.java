package com.example.TCC.service;

import com.example.TCC.domain.*;
import com.example.TCC.dto.response.TryChallengeResponseDto;
import com.example.TCC.exception.NotFoundException;
import com.example.TCC.manager.tryChall.TryChallRetriever;
import com.example.TCC.manager.tryChall.TryChallSaver;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TryChallService {

    private final TryChallRetriever tryChallRetriever;
    private final TryChallSaver tryChallSaver;

    @Transactional
    // 챌린지 도전
    public void tryChallenge(Member member) {
        TryChall tryChall = tryChallRetriever.findByMember(member);

        if (tryChall.getStartTime() != null) {
            LocalDateTime expireTime = tryChall.getStartTime().plus(Duration.ofHours(24));
            tryChall.updateExpireTime(expireTime);
        } else {
            throw new NotFoundException("챌린지 시작시간이 설정되어 있지 않습니다.");
        }

        tryChallSaver.save(tryChall);
    }

    @Transactional(readOnly = true)
    // 챌린지 도전항목 조회
    public TryChallengeResponseDto tryCheck(Member member) {
        TryChall tryChall = tryChallRetriever.findByMember(member);

        if (tryChall.getExpireTime() == null)
            throw new NotFoundException("도전 중인 챌린지가 없습니다.");

        return TryChallengeResponseDto.createTryChallengeDto(tryChall);
    }
}