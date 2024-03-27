package com.example.TCC.service;

import com.example.TCC.domain.*;
import com.example.TCC.dto.request.DrawChallengeRequestDto;
import com.example.TCC.dto.response.CompleteChallengeResponseDto;
import com.example.TCC.dto.response.DrawChallengeResponseDto;
import com.example.TCC.dto.response.TryChallengeResponseDto;
import com.example.TCC.exception.ConflictException;
import com.example.TCC.exception.NotFoundException;
import com.example.TCC.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ChallengeService {

    private final TryChallRepository tryChallRepository;
    private final ChallengeRepository challengeRepository;
    private final CategoryRepository categoryRepository;
    private final CompleteChallRepository completeChallRepository;

    //챌린지 뽑기
    public DrawChallengeResponseDto draw(DrawChallengeRequestDto dto, Member member) {

        //이미 도전 중인 챌린지가 있는지 확인
        if (tryChallRepository.existsByMemberAndExpireTimeIsNotNull(member)) {
            throw new ConflictException("이미 도전 중인 챌린지가 있습니다.");
        }

        //기존에 뽑기만 한 챌린지 삭제
        Optional<TryChall> existingChallenge = tryChallRepository.findByMemberAndExpireTimeIsNull(member);
        existingChallenge.ifPresent(tryChallRepository::delete);

        String categoryName = dto.getCategory();

        List<Challenge> challenges;

        //랜덤을 선택한 경우 모든 챌린지를 조회
        if (categoryName.equals("랜덤")) {
            challenges = challengeRepository.findAll();
        } else {
            // 특정 카테고리가 지정된 경우, 해당 카테고리에 속하는 챌린지 조회
            Category category = categoryRepository.findByCategoryName(categoryName)
                    .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다."));
            challenges = challengeRepository.findByCategory(category);
        }

        if (challenges.isEmpty()) {
            throw new NotFoundException("카테고리에 해당하는 챌린지가 없습니다.");
        }

        //챌린지 리스트에서 랜덤하게 하나 선택
        Random random = new Random();
        Challenge selectedChallenge = challenges.get(random.nextInt(challenges.size()));

        //선택된 챌린지를 바탕으로 Trychall 객체 생성 및 저장
        TryChall tryChall = TryChall.create(selectedChallenge, member);
        TryChall draw = tryChallRepository.save(tryChall);

        return DrawChallengeResponseDto.createDrawChallengeDto(draw);
    }

    //챌린지 도전
    @Transactional
    public void tryChallenge(Member member) {
        //멤버에 해당하는 챌린지 가져오기
        TryChall tryChall = tryChallRepository.findByMember(member)
                .orElseThrow( () -> new NotFoundException("챌린지를 찾을 수 없습니다."));

        //만료시간 설정해주기 (시작시간으로부터 24시간 뒤)
        if (tryChall.getStartTime() != null) {
            LocalDateTime expireTime = tryChall.getStartTime().plus(Duration.ofHours(24));
            tryChall.setExpireTime(expireTime);
        } else {
            //시작시간이 설정되어 있지 않는 경우
            throw new NotFoundException("챌린지 시작시간이 설정되어 있지 않습니다.");
        }

        tryChallRepository.save(tryChall); //변경된 챌린지 정보 저장
    }

    //챌린지 도전항목 조회
    public TryChallengeResponseDto tryCheck(Member member) {

        //멤버에 해당하는 챌린지 가져오기
        TryChall tryChall = tryChallRepository.findByMember(member)
                .orElseThrow( () -> new NotFoundException("도전 중인 챌린지가 없습니다."));

        //만료시간이 null이면 조회 못하게 해야함 (뽑기만 하고
        if (tryChall.getExpireTime() == null)
            throw new NotFoundException("도전 중인 챌린지가 없습니다.");

        TryChallengeResponseDto dto = TryChallengeResponseDto.createTryChallengeDto(tryChall);

        return dto;
    }

    //챌린지 완료
    public CompleteChallengeResponseDto complete(Member member) {

        //멤버에 해당하는 챌린지 가져오기
        TryChall tryChall = tryChallRepository.findByMember(member)
                .orElseThrow( () -> new NotFoundException("완료할 챌린지가 없습니다."));

        if (tryChall.getExpireTime() == null)
            throw new NotFoundException("도전 중인 챌린지가 없습니다.");

        CompleteChall completeChall = CompleteChall.create(tryChall);
        CompleteChall complete = completeChallRepository.save(completeChall);

        tryChallRepository.delete(tryChall);

        CompleteChallengeResponseDto dto = CompleteChallengeResponseDto.createCompleteChallengeDto(complete);

        return dto;
    }

    //완료한 챌린지 전체 조회
    public List<CompleteChallengeResponseDto> showAll(Member member) {
        Long memberId = member.getId();

        List<CompleteChall> challenges = completeChallRepository.findAllByMemberId(memberId);

        List<CompleteChallengeResponseDto> dtos = new ArrayList<CompleteChallengeResponseDto>();
        for (CompleteChall c : challenges) {
            CompleteChallengeResponseDto dto = CompleteChallengeResponseDto.createCompleteChallengeDto(c);
            dtos.add(dto);
        }

        return dtos;
    }
}