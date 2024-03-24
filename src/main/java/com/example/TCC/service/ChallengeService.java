package com.example.TCC.service;

import com.example.TCC.domain.Category;
import com.example.TCC.domain.Challenge;
import com.example.TCC.domain.Member;
import com.example.TCC.domain.TryChall;
import com.example.TCC.dto.request.DrawChallengeRequestDto;
import com.example.TCC.dto.response.DrawChallengeResponseDto;
import com.example.TCC.exception.ConflictException;
import com.example.TCC.exception.NotFoundException;
import com.example.TCC.repository.CategoryRepository;
import com.example.TCC.repository.ChallengeRepository;
import com.example.TCC.repository.TryChallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final TryChallRepository tryChallRepository;
    private final ChallengeRepository challengeRepository;
    private final CategoryRepository categoryRepository;

    //챌린지 뽑기
    public DrawChallengeResponseDto draw(DrawChallengeRequestDto dto, Member member) {

        //이미 도전 중인 챌린지가 있는지 확인
        if (tryChallRepository.existsByMemberAndExpireTimeIsNotNull(member)) {
            throw new ConflictException("이미 도전 중인 챌린지가 있습니다.");
        }

        //기존에 뽑기만 한 챌린지 삭제
        Optional<TryChall> existingChallenge = tryChallRepository.findByMemberAndExpireTimeIsNull(member);
        existingChallenge.ifPresent(tryChallRepository::delete);

        //새로운 챌린지 저장
        Category category = categoryRepository.findByCategoryName(dto.getCategory());

        if (category != null) {
            // 해당 카테고리에 속하는 모든 챌린지 조회
            List<Challenge> challenges = challengeRepository.findByCategory(category);

            if (!challenges.isEmpty()) {
                // 챌린지 리스트에서 랜덤하게 하나 선택
                Random random = new Random();
                Challenge selectedChallenge = challenges.get(random.nextInt(challenges.size()));

                // 선택된 챌린지를 바탕으로 TryChall 객체 생성 및 저장
                TryChall tryChall = TryChall.create(selectedChallenge, member); // TryChall.create 메서드 구현에 따라 다를 수 있음
                TryChall draw = tryChallRepository.save(tryChall);
                return DrawChallengeResponseDto.createDrawChallengeDto(draw);
            } else {
                // 해당 카테고리에 챌린지가 없는 경우의 처리
                throw new NotFoundException("카테고리에 해당하는 챌린지가 없습니다.");
            }
        } else {
            // 카테고리를 찾을 수 없는 경우의 처리
            throw new NotFoundException("카테고리를 찾을 수 없습니다.");
        }
    }
}