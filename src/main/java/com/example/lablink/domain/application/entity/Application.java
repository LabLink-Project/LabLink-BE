package com.example.lablink.domain.application.entity;

import com.example.lablink.global.timestamp.entity.Timestamped;
import com.example.lablink.domain.user.entity.User;
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
@Table(name = "application",
        indexes = {@Index(name = "index_user",  columnList="user_id", unique = false),
                @Index(name = "index_study", columnList="studyId", unique = false)})
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

//    @Column(nullable = true)
//    @Enumerated(value = EnumType.STRING)
    private String applicationViewStatusEnum; // 기업이 내 신청서에 대한 "열람", "미열람"
//
//    @Column(nullable = true)
//    @Enumerated(value = EnumType.STRING)
    private String approvalStatusEnum; // 신청서 승인, 거절, 대기

    public Application(User user, Long study, String message, String approvalStatusEnum, String applicationViewStatusEnum) {
        this.user = user;
        this.studyId = study;
        this.message = message;
        this.approvalStatusEnum = approvalStatusEnum;
        this.applicationViewStatusEnum = applicationViewStatusEnum;
    }

    public void update(String message) {
        this.message = message;
    }

//    Enum 리팩토링시 사용 가능할듯
//    public String getApprovalStatusEnum() {
//        return this.approvalStatusEnum != null ? this.approvalStatusEnum.approvalGetStatus() : null;
//    }

//    public String getViewStatusEnum() {
//        return this.approvalStatusEnum != null ? this.applicationViewStatusEnum.viewGetStatus() : null;
//    }
//
    // 승인, 거절 업데이트
    public void statusUpdate(String approvalStatusEnum) {
        this.approvalStatusEnum = approvalStatusEnum;
    }

    // 열람, 미열람 업데이트
    public void viewStatusUpdate(String applicationViewStatusEnum) {
        this.applicationViewStatusEnum = applicationViewStatusEnum;
    }
}
