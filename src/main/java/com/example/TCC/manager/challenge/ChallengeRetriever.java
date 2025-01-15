package com.example.TCC.manager.challenge;

import com.example.TCC.domain.Category;
import com.example.TCC.domain.Challenge;
import com.example.TCC.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChallengeRetriever {

    private final ChallengeRepository challengeRepository;

    public List<Challenge> findAll() {
        return challengeRepository.findAll();
    }

    public List<Challenge> findByCategory(final Category category) {
        return challengeRepository.findByCategory(category);
    }
}
