package com.example.lablink.domain.study.entity;

import com.example.lablink.global.timestamp.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SearchRankKeyword extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // rankKeyword, score
    @Column(nullable = false)
    private String rankKeyword;

    @Column(nullable = false)
    private Double score;
}
