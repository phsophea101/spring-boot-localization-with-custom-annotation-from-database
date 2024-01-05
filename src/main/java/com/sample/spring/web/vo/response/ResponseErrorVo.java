package com.sample.spring.web.vo.response;

import org.apache.commons.lang3.ObjectUtils;

public class ResponseErrorVo {
    private String code;
    private String message;
    private Object descriptions;

    public ResponseErrorVo(String code, String message) {
        this.code = code;
        this.message = message;
        this.descriptions = message;
    }

    public ResponseErrorVo(String code, String message, Object description) {
        this.code = code;
        this.message = message;
        this.descriptions = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDescriptions() {
        return ObjectUtils.isEmpty(descriptions) ? message : descriptions;
    }

    public void setDescriptions(Object descriptions) {
        this.descriptions = descriptions;
    }
}
