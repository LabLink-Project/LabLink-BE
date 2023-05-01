package com.example.lablink.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTerms is a Querydsl query type for Terms
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTerms extends EntityPathBase<Terms> {

    private static final long serialVersionUID = 2061997319L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTerms terms = new QTerms("terms");

    public final com.example.lablink.timestamp.entity.QTimestamped _super = new com.example.lablink.timestamp.entity.QTimestamped(this);

    public final BooleanPath ageCheck = createBoolean("ageCheck");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath marketingOptIn = createBoolean("marketingOptIn");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final BooleanPath privacyPolicyConsent = createBoolean("privacyPolicyConsent");

    public final BooleanPath sensitiveInfoConsent = createBoolean("sensitiveInfoConsent");

    public final BooleanPath termsOfServiceAgreement = createBoolean("termsOfServiceAgreement");

    public final QUser user;

    public QTerms(String variable) {
        this(Terms.class, forVariable(variable), INITS);
    }

    public QTerms(Path<? extends Terms> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTerms(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTerms(PathMetadata metadata, PathInits inits) {
        this(Terms.class, metadata, inits);
    }

    public QTerms(Class<? extends Terms> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

