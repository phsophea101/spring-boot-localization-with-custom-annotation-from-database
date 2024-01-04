package com.sample.spring.entity;

import com.sample.spring.common.model.AuditEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
@ToString
@Document(collection = AuditTrailFieldEntity.TABLE_NAME)
public class AuditTrailPrimaryKeyEntity extends AuditEntity {
    public static final String TABLE_NAME = "audit_trail_primary_keys";
    @Field("field_name")
    private String fieldName;
    @Field("field_value")
    private String fieldValue;
}
