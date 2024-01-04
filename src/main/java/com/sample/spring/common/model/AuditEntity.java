package com.sample.spring.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Getter
@Setter
@FieldNameConstants
public class AuditEntity extends IdentityEntity {
    @Field("created_by")
    @CreatedBy
    protected String createdBy;
    @Field("created_date")
    @CreatedDate
    protected Date createdDate;
    @Field("updated_by")
    @LastModifiedBy
    protected String updatedBy;
    @Field("updated_date")
    @LastModifiedDate
    protected Date updatedDate;
}
