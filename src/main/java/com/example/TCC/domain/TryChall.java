package com.example.TCC.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class TryChall { //챌린지 도전중

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime expireTime;

    @Column
    private String nickname;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Challenge challenge;

    private TryChall(LocalDateTime startTime, LocalDateTime expireTime, String nickname, Member member, Challenge challenge) {
        this.startTime = startTime;
        this.expireTime = expireTime;
        this.nickname = nickname;
        this.member = member;
        this.challenge = challenge;
    }

    public static TryChall create(Challenge challenge, Member member) {

        return new TryChall(
                LocalDateTime.now(),
                null,
                member.getNickname(),
                member,
                challenge
        );
    }

    public void updateExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
