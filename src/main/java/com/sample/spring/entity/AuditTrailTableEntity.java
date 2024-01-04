package com.sample.spring.entity;

import com.sample.spring.common.model.AuditEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Setter
@Getter
@Document(collection = AuditTrailTableEntity.TABLE_NAME)
public class AuditTrailTableEntity extends AuditEntity {
    public static final String TABLE_NAME = "audit_trail_primary_keys";
    @Field("trace_id")
    private String traceId;
    @Field("table_name")
    private String tableName;
    @Field("primary_keys")
    private List<AuditTrailPrimaryKeyEntity> primaryKeys;
}
