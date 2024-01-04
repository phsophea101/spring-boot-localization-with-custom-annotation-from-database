package com.sample.spring.common.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuditDto {
    protected String id;
    protected String createdBy;
    protected Date createdDate = new Date();
    protected String updatedBy;
    protected Date updatedDate;
}
