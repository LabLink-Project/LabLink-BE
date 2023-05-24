package com.example.lablink.domain.user.repository;

import com.example.lablink.domain.user.dto.response.MyLabResponseDto;
import com.example.lablink.domain.application.entity.QApplication;
import com.example.lablink.domain.study.entity.QStudy;
import com.example.lablink.domain.user.entity.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QStudy qStudy = QStudy.study;
    private final QApplication qApplication = QApplication.application;

    public UserQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<MyLabResponseDto> getMyLabResponseDto(User userDetails) {
        return queryFactory
                .select(Projections.constructor(
                        MyLabResponseDto.class,
                        qStudy.id,
                        qStudy.title,
                        qStudy.createdAt,
                        qStudy.pay,
                        qStudy.address,
                        qApplication.applicationViewStatusEnum,
                        qApplication.approvalStatusEnum,
                        qStudy.date,
                        qStudy.company.companyName))
                .from(qStudy)
                .leftJoin(qApplication)
                .on(qStudy.id.eq(qApplication.studyId))
                .where(qApplication.user.eq(userDetails))
                .fetch();
    }
}
