package com.example.TCC.manager.tryChall;

import com.example.TCC.domain.Member;
import com.example.TCC.domain.TryChall;
import com.example.TCC.exception.NotFoundException;
import com.example.TCC.repository.TryChallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TryChallRetriever {

    private final TryChallRepository tryChallRepository;

    public boolean existsByMemberAndExpireTimeIsNotNull(final Member member) {
        return tryChallRepository.existsByMemberAndExpireTimeIsNotNull(member);
    }

    public Optional<TryChall> findByMemberAndExpireTimeIsNull(final Member member) {
        return tryChallRepository.findByMemberAndExpireTimeIsNull(member);
    }

    public TryChall findByMember(final Member member) {
        return tryChallRepository.findByMember(member)
                .orElseThrow(() -> new NotFoundException("챌린지를 찾을 수 없습니다."));
    }
}
