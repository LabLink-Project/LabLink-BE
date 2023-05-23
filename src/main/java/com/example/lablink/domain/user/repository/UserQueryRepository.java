package com.example.lablink.domain.user.repository;

import com.example.lablink.domain.user.dto.response.MyLabResponseDto;
import com.example.lablink.domain.user.entity.QUser;
import com.example.lablink.domain.application.entity.QApplication;
import com.example.lablink.domain.study.entity.QStudy;
import com.example.lablink.domain.user.entity.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

// 내 신청서 목록 조회 querydsl을 위한 클래스 ( 비교 결과 jpql과 큰 차이는 없다 )
@Repository
public class UserQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QUser qUser = QUser.user;
    private final QStudy qStudy = QStudy.study;
    private final QApplication qApplication = QApplication.application;

    public UserQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<MyLabResponseDto> getMyLabResponseDto(User userDetails) {
        return queryFactory
                .select(Projections.constructor(MyLabResponseDto.class, qStudy, qApplication.applicationViewStatusEnum, qApplication.approvalStatusEnum))
                .from(qStudy)
                .leftJoin(qApplication)
                .on(qStudy.id.eq(qApplication.studyId))
                .where(qApplication.user.eq(userDetails))
                .fetch();
    }
}
