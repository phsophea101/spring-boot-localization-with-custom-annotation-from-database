package com.sample.spring.web.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sample.spring.web.vo.request.RequestPageableVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

public class ResponsePageableVO<T> {
    protected static final long DEFAULT_RECORDS = NumberUtils.LONG_ZERO;
    private String records = String.valueOf(DEFAULT_RECORDS);
    private List<T> items;
    private String pages;
    private String page;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRecordFrom() {
        return recordFrom;
    }

    public void setRecordFrom(String recordFrom) {
        this.recordFrom = recordFrom;
    }

    public String getRecordTo() {
        return recordTo;
    }

    public void setRecordTo(String recordTo) {
        this.recordTo = recordTo;
    }

    @JsonProperty("record_from")
    private String recordFrom;
    @JsonProperty("record_to")
    private String recordTo;

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }

    public ResponsePageableVO(int records, List<T> items, RequestPageableVO pageable) {
        this((long) records, items, pageable);
    }

    public ResponsePageableVO(long records, List<T> items, RequestPageableVO pageable) {
        this.records = String.valueOf(records);
        this.items = items;
        this.pages = String.valueOf((int) Math.ceil((double) Integer.parseInt(this.records) / pageable.getRpp()));
        this.page = String.valueOf(pageable.getPage());
        if (ObjectUtils.isEmpty(this.items)) {
            this.recordFrom = String.valueOf(1);
            this.recordTo = String.valueOf(Math.toIntExact(Integer.parseInt(this.records)));
        } else {
            this.recordFrom = String.valueOf(Integer.parseInt(this.page) * pageable.getRpp() - pageable.getRpp() + 1);
            this.recordTo = String.valueOf((Integer.valueOf(this.page).equals(Integer.valueOf(this.pages)) ? Integer.parseInt(this.records) : Integer.parseInt(this.page) * pageable.getRpp()));
        }
    }
}