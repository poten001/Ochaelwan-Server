package com.example.TCC.manager.completeChall;

import com.example.TCC.domain.CompleteChall;
import com.example.TCC.repository.CompleteChallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompleteChallSaver {

    private final CompleteChallRepository completeChallRepository;

    public CompleteChall save(final CompleteChall completeChall) {
        return completeChallRepository.save(completeChall);
    }
}
