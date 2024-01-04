package com.sample.spring.common.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ModelQueryDto {
    private List<QueryModelDto> queries;
    private List<String> fields;
    private PageableDto pageable;

    public ModelQueryDto() {
    }

    public ModelQueryDto(PageableDto pageable) {
        this.pageable = pageable;
    }

    public ModelQueryDto(List<QueryModelDto> queries) {
        this.queries = queries;
    }

    public ModelQueryDto(List<QueryModelDto> queries, List<String> fields) {
        this.queries = queries;
        this.fields = fields;
    }

    public ModelQueryDto(List<QueryModelDto> queries, List<String> fields, PageableDto pageable) {
        this.queries = queries;
        this.fields = fields;
        this.pageable = pageable;
    }

    public ModelQueryDto(List<QueryModelDto> queries, PageableDto pageable) {
        this.queries = queries;
        this.pageable = pageable;
    }
}
