package com.example.lablink.feedback.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFeedback is a Querydsl query type for Feedback
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeedback extends EntityPathBase<Feedback> {

    private static final long serialVersionUID = -779350081L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFeedback feedback = new QFeedback("feedback");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath feedbackMessage = createString("feedbackMessage");

    public final NumberPath<Long> Id = createNumber("Id", Long.class);

    public final com.example.lablink.study.entity.QStudy study;

    public final com.example.lablink.user.entity.QUser user;

    public final BooleanPath viewStatus = createBoolean("viewStatus");

    public QFeedback(String variable) {
        this(Feedback.class, forVariable(variable), INITS);
    }

    public QFeedback(Path<? extends Feedback> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFeedback(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFeedback(PathMetadata metadata, PathInits inits) {
        this(Feedback.class, metadata, inits);
    }

    public QFeedback(Class<? extends Feedback> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.study = inits.isInitialized("study") ? new com.example.lablink.study.entity.QStudy(forProperty("study"), inits.get("study")) : null;
        this.user = inits.isInitialized("user") ? new com.example.lablink.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

