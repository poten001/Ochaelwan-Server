package com.example.TCC.repository;

import com.example.TCC.domain.Member;
import com.example.TCC.domain.TryChall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TryChallRepository extends JpaRepository<TryChall, Long> {

    boolean existsByMemberAndExpireTimeIsNotNull(Member member);

    Optional<TryChall> findByMemberAndExpireTimeIsNull(Member member);

    Optional<TryChall> findByMember(Member member);
}
