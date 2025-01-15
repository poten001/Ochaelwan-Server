package com.example.TCC.manager.completeChall;

import com.example.TCC.domain.CompleteChall;
import com.example.TCC.exception.NotFoundException;
import com.example.TCC.repository.CompleteChallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompleteChallRetriever {

    private final CompleteChallRepository completeChallRepository;

    public List<CompleteChall> findAllByMemberIdOrderByCompleteTimeDesc(final Long memberId) {
        return completeChallRepository.findAllByMemberIdOrderByCompleteTimeDesc(memberId);
    }

    public CompleteChall findByIdAndMemberId(final Long id, final Long memberId) {
        return completeChallRepository.findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new NotFoundException("해당하는 챌린지 완료 정보를 찾을 수 없습니다."));
    }
}
