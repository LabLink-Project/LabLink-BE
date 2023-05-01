package com.example.lablink.study.repository;

import com.example.lablink.study.entity.QStudy;
import com.example.lablink.study.entity.Study;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Repository
public class StudySearchQueryRepository {
    private final JPAQueryFactory queryFactory;

    public StudySearchQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    QStudy study = QStudy.study;

    public List<Study> searchStudies(String keyword, int pageIndex, int pageCount) {
        return queryFactory
                .selectFrom(study)
                .where(
                        searchCondition(keyword)
                )
                .orderBy(study.id.desc())
                .offset(pageIndex * pageCount)
                .limit(pageCount)
                .fetch();
    }


    private BooleanExpression searchCondition(String keyword){
        if(StringUtils.isEmpty(keyword)){
            return null;
        }
        return study.title.containsIgnoreCase(keyword);
    }

    /*private BooleanExpression searchCondition(String keyword) {
        BooleanExpression titleCondition = keyword != null ? study.title.containsIgnoreCase(keyword) : null;
//        log.info("study.title : " + study.title);
//        log.info("titleCondition : " + titleCondition);
//        BooleanExpression studyInfoCondition = keyword != null ?  study.studyInfo.containsIgnoreCase(keyword) : null;
//        BooleanExpression studyPurposeCondition = keyword != null ? study.studyPurpose.containsIgnoreCase(keyword) : null;
//        BooleanExpression studyActionCondition = keyword != null ? study.studyAction.containsIgnoreCase(keyword) : null;

//        if (titleCondition == null && studyInfoCondition == null && studyPurposeCondition == null && studyActionCondition == null) {
//            return null;
//        }

        return titleCondition;
//                .or(studyInfoCondition)
//                .or(studyPurposeCondition)
//                .or(studyActionCondition);
    }*/

}
