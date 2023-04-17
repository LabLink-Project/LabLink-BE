package com.example.lablink.bookmark.entity;

import com.example.lablink.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studyId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Bookmark(Long studyId, User user) {
        this.studyId = studyId;
        this.user = user;
    }
}
