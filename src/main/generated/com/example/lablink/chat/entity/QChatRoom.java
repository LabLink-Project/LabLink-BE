package com.example.lablink.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = -1763570080L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.lablink.company.entity.QCompany owner;

    public final StringPath roomId = createString("roomId");

    public final com.example.lablink.study.entity.QStudy study;

    public final com.example.lablink.user.entity.QUser user;

    public QChatRoom(String variable) {
        this(ChatRoom.class, forVariable(variable), INITS);
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoom(PathMetadata metadata, PathInits inits) {
        this(ChatRoom.class, metadata, inits);
    }

    public QChatRoom(Class<? extends ChatRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new com.example.lablink.company.entity.QCompany(forProperty("owner")) : null;
        this.study = inits.isInitialized("study") ? new com.example.lablink.study.entity.QStudy(forProperty("study"), inits.get("study")) : null;
        this.user = inits.isInitialized("user") ? new com.example.lablink.user.entity.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

