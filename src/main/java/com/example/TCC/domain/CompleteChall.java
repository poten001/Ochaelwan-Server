package com.example.TCC.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    public static CompleteChall create(TryChall tryChall) {

        return new CompleteChall(
                null,
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
