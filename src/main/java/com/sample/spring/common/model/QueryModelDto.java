package com.sample.spring.common.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QueryModelDto {
    private String fieldName;
    private QueryCondition condition;
    private Object fieldValue;

    public QueryModelDto(String fieldName, QueryCondition condition, Object fieldValue) {
        this.fieldName = fieldName;
        this.condition = condition;
        this.fieldValue = fieldValue;
    }
}
