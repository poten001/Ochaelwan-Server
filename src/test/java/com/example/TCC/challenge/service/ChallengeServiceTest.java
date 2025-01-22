package com.example.TCC.challenge.service;

import com.example.TCC.domain.Category;
import com.example.TCC.domain.Challenge;
import com.example.TCC.domain.Member;
import com.example.TCC.domain.TryChall;
import com.example.TCC.dto.request.DrawChallengeRequestDto;
import com.example.TCC.dto.response.DrawChallengeResponseDto;
import com.example.TCC.exception.ConflictException;
import com.example.TCC.manager.challenge.ChallengeRetriever;
import com.example.TCC.manager.tryChall.TryChallRemover;
import com.example.TCC.manager.tryChall.TryChallRetriever;
import com.example.TCC.manager.tryChall.TryChallSaver;
import com.example.TCC.service.ChallengeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ChallengeServiceTest {

    @InjectMocks
    private ChallengeService challengeService;

    @Mock
    private TryChallRetriever tryChallRetriever;

    @Mock
    private TryChallSaver tryChallSaver;

    @Mock
    private ChallengeRetriever challengeRetriever;

    @Mock
    private TryChallRemover tryChallRemover;

    private Member mockMember;
    private Category mockCategory;
    private Challenge mockChallenge;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockMember = new Member();
        mockCategory = Category.builder().build();
        mockChallenge = Challenge
                .builder()
                .title("Test Challenge")
                .category(mockCategory)
                .build();
    }

    @Test
    @DisplayName("챌린지 뽑기가 정상적으로 동작한다.")
    void draw() {
        //given
        DrawChallengeRequestDto dto = new DrawChallengeRequestDto("랜덤");

        TryChall tryChall = TryChall.create(mockChallenge, mockMember);

        when(tryChallRetriever.existsByMemberAndExpireTimeIsNotNull(mockMember)).thenReturn(false);
        when(tryChallRetriever.findByMemberAndExpireTimeIsNull(mockMember)).thenReturn(Optional.empty());
        when(challengeRetriever.findAll()).thenReturn(Collections.singletonList(mockChallenge));
        when(tryChallSaver.save(any())).thenReturn(tryChall);

        //when
        DrawChallengeResponseDto response = challengeService.draw(dto, mockMember);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getChallengeTitle()).isEqualTo("Test Challenge");

        verify(tryChallRetriever, times(1)).existsByMemberAndExpireTimeIsNotNull(mockMember);
        verify(tryChallRetriever, times(1)).findByMemberAndExpireTimeIsNull(mockMember);
        verify(challengeRetriever, times(1)).findAll();
        verify(tryChallSaver, times(1)).save(any());
    }

    @Test
    @DisplayName("이미 도전 중인 챌린지가 있으면 예외를 반환한다.")
    void draw_fail() {
        //given
        DrawChallengeRequestDto dto = new DrawChallengeRequestDto("랜덤");

        when(tryChallRetriever.existsByMemberAndExpireTimeIsNotNull(mockMember)).thenReturn(true);

        //when & then
        assertThatThrownBy(() -> challengeService.draw(dto, mockMember))
        .isInstanceOf(ConflictException.class)
                .hasMessage("이미 도전 중인 챌린지가 있습니다.");

        verify(tryChallRetriever, times(1)).existsByMemberAndExpireTimeIsNotNull(mockMember);
        verify(challengeRetriever, never()).findAll();
    }

}
