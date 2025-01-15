package com.example.TCC.manager.challenge;

import com.example.TCC.domain.Challenge;
import com.example.TCC.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChallengeSaver {

    private final ChallengeRepository challengeRepository;

    public Challenge save(final Challenge challenge) {
        return challengeRepository.save(challenge);
    }
}
