package com.example.TCC.service;

import com.example.TCC.domain.*;
import com.example.TCC.dto.request.DrawChallengeRequestDto;
import com.example.TCC.dto.response.DrawChallengeResponseDto;
import com.example.TCC.exception.ConflictException;
import com.example.TCC.exception.NotFoundException;
import com.example.TCC.manager.category.CategoryRetriever;
import com.example.TCC.manager.challenge.ChallengeRetriever;
import com.example.TCC.manager.tryChall.TryChallRemover;
import com.example.TCC.manager.tryChall.TryChallRetriever;
import com.example.TCC.manager.tryChall.TryChallSaver;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final TryChallRetriever tryChallRetriever;
    private final TryChallSaver tryChallSaver;
    private final TryChallRemover tryChallRemover;
    private final ChallengeRetriever challengeRetriever;
    private final CategoryRetriever categoryRetriever;

    @Transactional
    // 챌린지 뽑기
    public DrawChallengeResponseDto draw(DrawChallengeRequestDto dto, Member member) {
        // 이미 도전 중인 챌린지가 있는지 확인
        validateExistingChallenge(member);
        // 기존에 뽑기만 한 챌린지 삭제
        removeExistingChallengeIfAny(member);

        String categoryName = dto.getCategory();
        // 챌린지 목록 가져오기
        List<Challenge> challenges = fetchChallenges(categoryName);

        // 랜덤으로 챌린지 선택
        Challenge selectedChallenge = selectRandomChallenge(challenges);
        // 선택된 챌린지 저장
        TryChall draw = saveSelectedChallenge(selectedChallenge, member);

        return DrawChallengeResponseDto.createDrawChallengeDto(draw);
    }

    // 이미 도전 중인 챌린지가 있는지 확인하고 예외 처리
    private void validateExistingChallenge(Member member) {
        if (tryChallRetriever.existsByMemberAndExpireTimeIsNotNull(member)) {
            throw new ConflictException("이미 도전 중인 챌린지가 있습니다.");
        }
    }

    // 기존에 뽑기만 한 챌린지가 있으면 삭제
    private void removeExistingChallengeIfAny(Member member) {
        Optional<TryChall> existingChallenge = tryChallRetriever.findByMemberAndExpireTimeIsNull(member);
        existingChallenge.ifPresent(tryChallRemover::delete);
    }

    // 카테고리에 따라 챌린지 목록 가져오기
    private List<Challenge> fetchChallenges(String categoryName) {
        if ("랜덤".equals(categoryName)) {
            return challengeRetriever.findAll();
        } else {
            Category category = categoryRetriever.findByCategoryName(categoryName);
            List<Challenge> challenges = challengeRetriever.findByCategory(category);
            if (challenges.isEmpty()) {
                throw new NotFoundException("카테고리에 해당하는 챌린지가 없습니다.");
            }
            return challenges;
        }
    }

    // 랜덤으로 챌린지 선택
    private Challenge selectRandomChallenge(List<Challenge> challenges) {
        Random random = new Random();
        return challenges.get(random.nextInt(challenges.size()));
    }

    // 선택된 챌린지 저장
    private TryChall saveSelectedChallenge(Challenge selectedChallenge, Member member) {
        TryChall tryChall = TryChall.create(selectedChallenge, member);
        return tryChallSaver.save(tryChall);
    }
}
