package com.sample.spring.common.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Setter
@Getter
public class PageableDto {
    private Sort sort;
    private Integer rpp = 10;
    private Integer page = 1;
    private Integer offset;

    public PageableDto() {
    }

    public PageableDto(Integer rpp, Integer page) {
        this.rpp = rpp;
        this.page = page;
    }

    public PageableDto(Sort sort) {
        this.sort = sort;
    }

    public int getOffset() {
        return (getPage() - 1) * getRpp();
    }
}
