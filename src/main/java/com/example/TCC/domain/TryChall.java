package com.example.TCC.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TryChall { //챌린지 도전중

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime expireTime;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Challenge challenge;

    public static TryChall create(Challenge challenge, Member member) {

        return new TryChall(
                null,
                LocalDateTime.now(),
                null,
                member,
                challenge
        );
    }
}
