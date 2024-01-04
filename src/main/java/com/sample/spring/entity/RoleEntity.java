package com.sample.spring.entity;

import com.sample.spring.common.model.AuditEntity;
import com.sample.spring.common.model.RecyclableEntity;
import com.sample.spring.enums.StatusType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Setter
@Getter
@ToString
@Document(collection = RoleEntity.TABLE_NAME)
public class RoleEntity extends AuditEntity implements RecyclableEntity<String> {
    public static final String TABLE_NAME = "roles";
    private String code;
    private String label;
    private Set<PermissionEntity> permissions;
    private String status = String.valueOf(StatusType.ACTIVE);
}
