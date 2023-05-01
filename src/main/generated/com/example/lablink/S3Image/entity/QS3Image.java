package com.example.lablink.S3Image.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QS3Image is a Querydsl query type for S3Image
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QS3Image extends EntityPathBase<S3Image> {

    private static final long serialVersionUID = 1103494171L;

    public static final QS3Image s3Image = new QS3Image("s3Image");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath originalFileName = createString("originalFileName");

    public final StringPath uploadFileName = createString("uploadFileName");

    public final StringPath uploadFilePath = createString("uploadFilePath");

    public final StringPath uploadFileUrl = createString("uploadFileUrl");

    public QS3Image(String variable) {
        super(S3Image.class, forVariable(variable));
    }

    public QS3Image(Path<? extends S3Image> path) {
        super(path.getType(), path.getMetadata());
    }

    public QS3Image(PathMetadata metadata) {
        super(S3Image.class, metadata);
    }

}

