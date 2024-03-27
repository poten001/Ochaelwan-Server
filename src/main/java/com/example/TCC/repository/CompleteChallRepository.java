package com.example.TCC.repository;

import com.example.TCC.domain.CompleteChall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompleteChallRepository extends JpaRepository<CompleteChall, Long> {
    List<CompleteChall> findAllByMemberId(Long memberId);

    Optional<CompleteChall> findByIdAndMemberId(Long challengeId, Long memberId);
}
