package com.example.TCC.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompleteChall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Column
    private String nickname;

    @Column
    private String memberProfileImg;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime completeTime;

    @Column
    private Duration takenTime;

    @Column
    private String challengeTitle;

    @Column
    private String challengeImg;

//    @OneToOne
//    @JoinColumn(name = "tryChall_id")
//    private TryChall tryChall;


    private CompleteChall(Member member, String nickname, String memberProfileImg, LocalDateTime startTime, LocalDateTime completeTime, Duration takenTime, String challengeTitle, String challengeImg) {
        this.member = member;
        this.nickname = nickname;
        this.memberProfileImg = memberProfileImg;
        this.startTime = startTime;
        this.completeTime = completeTime;
        this.takenTime = takenTime;
        this.challengeTitle = challengeTitle;
        this.challengeImg = challengeImg;
    }

    public static CompleteChall create(TryChall tryChall) {

        return new CompleteChall(
                tryChall.getMember(),
                tryChall.getNickname(),
                tryChall.getMember().getProfileImg(),
                tryChall.getStartTime(),
                LocalDateTime.now(),
                Duration.between(tryChall.getStartTime(), LocalDateTime.now()),
                tryChall.getChallenge().getTitle(),
                tryChall.getChallenge().getCategory().getCategoryImg()
        );
    }
}
