package com.example.lablink.company.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCompany is a Querydsl query type for Company
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompany extends EntityPathBase<Company> {

    private static final long serialVersionUID = -2017288261L;

    public static final QCompany company = new QCompany("company");

    public final StringPath address = createString("address");

    public final StringPath business = createString("business");

    public final StringPath companyName = createString("companyName");

    public final StringPath detailAddress = createString("detailAddress");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath logoUrl = createString("logoUrl");

    public final StringPath managerPhone = createString("managerPhone");

    public final StringPath ownerName = createString("ownerName");

    public final StringPath password = createString("password");

    public final EnumPath<com.example.lablink.user.entity.UserRoleEnum> role = createEnum("role", com.example.lablink.user.entity.UserRoleEnum.class);

    public QCompany(String variable) {
        super(Company.class, forVariable(variable));
    }

    public QCompany(Path<? extends Company> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompany(PathMetadata metadata) {
        super(Company.class, metadata);
    }

}

