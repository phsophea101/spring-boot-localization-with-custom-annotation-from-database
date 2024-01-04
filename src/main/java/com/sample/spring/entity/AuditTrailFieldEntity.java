package com.sample.spring.entity;

import com.sample.spring.common.model.AuditEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Setter
@Getter
@ToString
@Document(collection = AuditTrailFieldEntity.TABLE_NAME)
public class AuditTrailFieldEntity extends AuditEntity {
    public static final String TABLE_NAME = "audit_trail_fields";
    @Field("field_name")
    private String fieldName;
    @Field("current_value")
    private String currentValue;
    @Field("previous_value")
    private String previousValue;
}
