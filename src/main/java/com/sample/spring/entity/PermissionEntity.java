package com.sample.spring.entity;

import com.sample.spring.common.model.AuditEntity;
import com.sample.spring.common.model.RecyclableEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Setter
@Getter
@Document(collection = PermissionEntity.TABLE_NAME)
public class PermissionEntity extends AuditEntity implements RecyclableEntity<String>, Serializable {
    public static final String TABLE_NAME = "roles";
    private String code;
    private String label;
    private String status;
}
