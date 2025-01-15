package com.example.TCC.service;

import com.example.TCC.manager.tryChall.TryChallRemover;
import com.example.TCC.repository.TryChallRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeExpireService {

    private final TryChallRemover tryChallRemover;

    //매초마다 실행되는 스케줄러
    @Scheduled(fixedRate = 1000)
    public void deleteExpiredChallenges() {
        //현재 시간보다 expireTime이 이전인 모든 TryChall 인스턴스 찾아 삭제
        LocalDateTime now = LocalDateTime.now();
        tryChallRemover.deleteByExpireTimeBefore(now);
    }
}
