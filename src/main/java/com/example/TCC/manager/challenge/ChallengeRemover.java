package com.example.TCC.manager.challenge;

import com.example.TCC.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChallengeRemover {

    private final ChallengeRepository challengeRepository;

    public void deleteById(final Long id) {
        challengeRepository.deleteById(id);
    }
}
