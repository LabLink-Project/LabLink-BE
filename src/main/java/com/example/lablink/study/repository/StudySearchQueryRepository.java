package com.example.lablink.study.repository;

import com.example.lablink.study.dto.StudySearchOption;
import com.example.lablink.study.entity.QStudy;
import com.example.lablink.study.entity.Study;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

    public List<Study> searchStudiesByKeyword(String keyword, int pageIndex, int pageCount) {
        return queryFactory
                .selectFrom(study)
                .where(
                        searchCondition(keyword)
                )
                .orderBy(study.id.desc())
                .offset((pageIndex -1) * pageCount)
                .limit(pageCount)
                .fetch();
    }

    private BooleanExpression searchCondition(String keyword) {
        BooleanExpression titleCondition = keyword != null ? study.title.containsIgnoreCase(keyword) : null;
        BooleanExpression studyInfoCondition = keyword != null ?  study.studyInfo.containsIgnoreCase(keyword) : null;
        BooleanExpression studyPurposeCondition = keyword != null ? study.studyPurpose.containsIgnoreCase(keyword) : null;
        BooleanExpression studyActionCondition = keyword != null ? study.studyAction.containsIgnoreCase(keyword) : null;

        return titleCondition
                .or(studyInfoCondition)
                .or(studyPurposeCondition)
                .or(studyActionCondition);
    }


    public List<Study> searchStudies(StudySearchOption searchOption, int pageIndex, int pageCount) {
        String category = searchOption.getCategory();
        String address = searchOption.getAddress();
//        LocalDate searchDate = searchOption.getSearchDate();
//        LocalTime searchTime = searchOption.getSearchTime();
        String gender = searchOption.getGender();
        String age = searchOption.getAge();

        QStudy study = QStudy.study;

//        BooleanExpression categoryExpression = category == null ? null : study.category.eq(CategoryEnum.valueOf(category));
        BooleanExpression categoryExpression = category == null ? null : study.category.stringValue().equalsIgnoreCase(category);

        BooleanExpression addressExpression = address == null ? null : study.address.containsIgnoreCase(address);

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        BooleanExpression searchDateExpression = searchDate == null ? null : study.date.stringValue().containsIgnoreCase(searchDate.format(formatter));
//
//        formatter = DateTimeFormatter.ofPattern("HH:mm");
//        BooleanExpression searchTimeExpression = searchTime == null ? null : study.date.stringValue().containsIgnoreCase(searchTime.format(formatter));

        BooleanExpression genderExpression = gender == null ? null : study.subjectGender.containsIgnoreCase(gender);
        BooleanExpression ageExpression = age == null ? null :
                study.subjectMinAge.loe(Integer.parseInt(age)).and(study.subjectMaxAge.goe(Integer.parseInt(age)));

        List<Study> studyList = queryFactory.selectFrom(study)
                .where(categoryExpression, addressExpression, genderExpression, ageExpression)
                .orderBy(study.createdAt.desc())
                .offset((pageIndex - 1) * pageCount)
                .limit(pageCount)
                .fetch();

        return studyList;
    }
}
