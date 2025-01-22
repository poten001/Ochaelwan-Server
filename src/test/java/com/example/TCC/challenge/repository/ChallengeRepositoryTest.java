package com.example.TCC.challenge.repository;

import com.example.TCC.domain.Category;
import com.example.TCC.domain.Challenge;
import com.example.TCC.repository.CategoryRepository;
import com.example.TCC.repository.ChallengeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class ChallengeRepositoryTest {

    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("챌린지를 저장하고 정상적으로 조회할 수 있다.")
    void save_and_findById() {
        //given
        Category category = Category.builder()
                .categoryName("운동").build();

        Challenge challenge = Challenge.builder()
                .title("동네 친구 만나서 줄넘기하기")
                .category(category)
                .build();

        Challenge savedChallenge = challengeRepository.save(challenge);

        //when
        Optional<Challenge> foundChallenge = challengeRepository.findById(savedChallenge.getId());

        //then
        assertThat(foundChallenge).isPresent();
        assertThat(foundChallenge.get().getTitle()).isEqualTo("동네 친구 만나서 줄넘기하기");
        assertThat(foundChallenge.get().getCategory().getCategoryName()).isEqualTo("운동");
    }

    @Test
    @DisplayName("특정 카테고리의 챌린지를 조회할 수 있다.")
    void findByCategory() {
        //given
        Category category = Category.builder()
                .categoryName("건강")
                .build();

        category = categoryRepository.save(category);

        Challenge challenge1 = createChallenge(category, "아침마다 스트레칭하기");
        Challenge challenge2 = createChallenge(category, "매일 물 2L 마시기");

        challengeRepository.save(challenge1);
        challengeRepository.save(challenge2);

        //when
        List<Challenge> foundChallenges = challengeRepository.findByCategory(category);

        //then
        assertThat(foundChallenges).hasSize(2);
        assertThat(foundChallenges).extracting(Challenge::getTitle)
                .containsExactlyInAnyOrder("아침마다 스트레칭하기", "매일 물 2L 마시기");
    }

    @Test
    @DisplayName("챌린지가 존재하지 않으면 빈 리스트를 반환한다.")
    void findByCategory_noResult() {
        //given
        Category category = Category.builder()
                .categoryName("여행")
                .build();

        category = categoryRepository.save(category);

        //when
        List<Challenge> foundChallenges = challengeRepository.findByCategory(category);

        //then
        assertThat(foundChallenges).isEmpty();
    }

    @Test
    @DisplayName("챌린지를 삭제하고 더 이상 조회되지 않아야 한다.")
    void deleteChallenge() {
        //given
        Category category = Category
                .builder()
                .categoryName("독서")
                .build();

        category = categoryRepository.save(category);

        Challenge challenge = createChallenge(category, "test");
        Challenge savedChallenge = challengeRepository.save(challenge);

        //when
        challengeRepository.deleteById(savedChallenge.getId());
        Optional<Challenge> foundChallenge = challengeRepository.findById(savedChallenge.getId());

        //then
        assertThat(foundChallenge).isNotPresent();
    }

    private Challenge createChallenge(Category category, String title) {
        return Challenge.builder()
                .title(title)
                .category(category)
                .build();
    }
}
