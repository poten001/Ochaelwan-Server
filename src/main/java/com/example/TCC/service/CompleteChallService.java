package com.example.TCC.service;

import com.example.TCC.domain.CompleteChall;
import com.example.TCC.domain.Member;
import com.example.TCC.domain.TryChall;
import com.example.TCC.dto.response.CompleteChallengeResponseDto;
import com.example.TCC.exception.NotFoundException;
import com.example.TCC.manager.completeChall.CompleteChallRetriever;
import com.example.TCC.manager.completeChall.CompleteChallSaver;
import com.example.TCC.manager.tryChall.TryChallRemover;
import com.example.TCC.manager.tryChall.TryChallRetriever;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompleteChallService {

    private final CompleteChallRetriever completeChallRetriever;
    private final CompleteChallSaver completeChallSaver;
    private final TryChallRetriever tryChallRetriever;
    private final TryChallRemover tryChallRemover;

    @Transactional
    // 챌린지 완료
    public CompleteChallengeResponseDto complete(Member member) {
        TryChall tryChall = tryChallRetriever.findByMember(member);

        if (tryChall.getExpireTime() == null)
            throw new NotFoundException("도전 중인 챌린지가 없습니다.");

        CompleteChall completeChall = CompleteChall.create(tryChall);
        CompleteChall complete = completeChallSaver.save(completeChall);

        tryChallRemover.delete(tryChall);

        return CompleteChallengeResponseDto.createCompleteChallengeDto(complete);
    }

    @Transactional(readOnly = true)
    // 완료한 챌린지 전체 조회
    public List<CompleteChallengeResponseDto> showAll(Member member) {
        Long memberId = member.getId();

        List<CompleteChall> challenges = completeChallRetriever.findAllByMemberIdOrderByCompleteTimeDesc(memberId);

        List<CompleteChallengeResponseDto> dtos = new ArrayList<>();
        for (CompleteChall c : challenges) {
            CompleteChallengeResponseDto dto = CompleteChallengeResponseDto.createCompleteChallengeDto(c);
            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional(readOnly = true)
    // 완료한 챌린지 상세 조회
    public CompleteChallengeResponseDto show(Long challengeId, Member member) {
        Long memberId = member.getId();

        CompleteChall completeChall = completeChallRetriever.findByIdAndMemberId(challengeId, memberId);

        return CompleteChallengeResponseDto.createCompleteChallengeDto(completeChall);
    }
}
