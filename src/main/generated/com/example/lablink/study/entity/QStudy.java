package com.example.lablink.study.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudy is a Querydsl query type for Study
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudy extends EntityPathBase<Study> {

    private static final long serialVersionUID = 1515694267L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStudy study = new QStudy("study");

    public final com.example.lablink.timestamp.entity.QTimestamped _super = new com.example.lablink.timestamp.entity.QTimestamped(this);

    public final StringPath address = createString("address");

    public final EnumPath<CategoryEnum> category = createEnum("category", CategoryEnum.class);

    public final com.example.lablink.company.entity.QCompany company;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> currentApplicantCount = createNumber("currentApplicantCount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final StringPath detailImageURL = createString("detailImageURL");

    public final BooleanPath emailSend = createBoolean("emailSend");

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final NumberPath<Integer> pay = createNumber("pay", Integer.class);

    public final NumberPath<Integer> repeatCount = createNumber("repeatCount", Integer.class);

    public final EnumPath<StudyStatusEnum> status = createEnum("status", StudyStatusEnum.class);

    public final StringPath studyAction = createString("studyAction");

    public final StringPath studyInfo = createString("studyInfo");

    public final StringPath studyPurpose = createString("studyPurpose");

    public final NumberPath<Long> subjectCount = createNumber("subjectCount", Long.class);

    public final StringPath subjectGender = createString("subjectGender");

    public final NumberPath<Integer> subjectMaxAge = createNumber("subjectMaxAge", Integer.class);

    public final NumberPath<Integer> subjectMinAge = createNumber("subjectMinAge", Integer.class);

    public final StringPath thumbnailImageURL = createString("thumbnailImageURL");

    public final StringPath title = createString("title");

    public QStudy(String variable) {
        this(Study.class, forVariable(variable), INITS);
    }

    public QStudy(Path<? extends Study> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStudy(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStudy(PathMetadata metadata, PathInits inits) {
        this(Study.class, metadata, inits);
    }

    public QStudy(Class<? extends Study> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new com.example.lablink.company.entity.QCompany(forProperty("company")) : null;
    }

}

