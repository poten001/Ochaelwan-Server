package com.example.TCC.repository;

import com.example.TCC.domain.Category;
import com.example.TCC.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByCategory(Category category);
}
