package com.example.TCC.manager.tryChall;

import com.example.TCC.domain.TryChall;
import com.example.TCC.repository.TryChallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TryChallSaver {

    private final TryChallRepository tryChallRepository;

    public TryChall save(final TryChall tryChall) {
        return tryChallRepository.save(tryChall);
    }
}
