package com.example.lablink.domain.bookmark.entity;

import com.example.lablink.domain.company.entity.Company;
import com.example.lablink.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studyId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "compnay_id", nullable = true)
    private Company company;

    private LocalDateTime deletedAt;

    public Bookmark(Long studyId, User user) {
        this.studyId = studyId;
        this.user = user;
    }

    public Bookmark(Long studyId, Company company) {
        this.studyId = studyId;
        this.company = company;
    }
}
