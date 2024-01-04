package com.sample.spring.entity;

import com.sample.spring.common.model.AuditEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Setter
@Getter
@ToString
@Document(collection = AuditTrailEntity.TABLE_NAME)
public class AuditTrailEntity extends AuditEntity {
    public static final String TABLE_NAME = "audit_trails";
    @Field("trace_id")
    private String traceId;
    @Field("table_name")
    private String tableName;
    private String user;
    private String action;
    @Field("service_name")
    private String serviceName;
    private List<RoleEntity> roles;
    private List<AuditTrailFieldEntity> fields;
    @Field("primary_keys")
    private List<AuditTrailPrimaryKeyEntity> primaryKeys;
}
