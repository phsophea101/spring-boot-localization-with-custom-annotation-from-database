package com.sample.spring.audit.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuditTrailDto {
    private String traceId;
    private String tableName;
    private String user;
    private String action;
    private String createdBy;
    private String serviceName;
    private Date createdDate;
    private List<RoleDto> roles;
    private List<AuditTrailFieldDto> fields;
    private List<AuditTrailPrimaryKeyDto> primaryKeys;
}
