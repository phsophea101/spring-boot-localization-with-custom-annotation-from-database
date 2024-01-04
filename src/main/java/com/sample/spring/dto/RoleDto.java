package com.sample.spring.dto;

import com.sample.spring.common.model.AuditDto;
import com.sample.spring.common.model.RecyclableEntity;
import com.sample.spring.entity.PermissionEntity;
import com.sample.spring.enums.StatusType;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class RoleDto extends AuditDto implements RecyclableEntity<String> {
    public static final String TABLE_NAME = "roles";
    private String code;
    private String labelDisplay;
    private Set<PermissionEntity> permissions;
    private String status;
}
