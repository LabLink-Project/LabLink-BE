package com.example.lablink.application.entity;

import com.example.lablink.timestamp.entity.Timestamped;
import com.example.lablink.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE application SET deleted_at = CONVERT_TZ(now(), 'UTC', 'Asia/Seoul') WHERE id = ?")
public class Application extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(nullable = false)
    private Long studyId;

    @Column(nullable = false)
    private String message;

    public Application(User user, Long study, String message) {
        this.user = user;
        this.studyId = study;
        this.message = message;
    }

    public void update(String message) {
        this.message = message;
    }

}
