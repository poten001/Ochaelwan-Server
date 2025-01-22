package com.example.TCC.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String categoryName;

    @Column
    private String categoryImg;

    @Builder
    public Category(String categoryName, String categoryImg) {
        this.categoryName = categoryName;
        this.categoryImg = categoryImg;
    }
}
