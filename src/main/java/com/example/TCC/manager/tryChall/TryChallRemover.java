package com.example.TCC.manager.tryChall;

import com.example.TCC.domain.TryChall;
import com.example.TCC.repository.TryChallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TryChallRemover {

    private final TryChallRepository tryChallRepository;

    public void delete(final TryChall tryChall) {
        tryChallRepository.delete(tryChall);
    }

    public void deleteByExpireTimeBefore(final LocalDateTime now) {
        tryChallRepository.deleteByExpireTimeBefore(now);
    }
}
