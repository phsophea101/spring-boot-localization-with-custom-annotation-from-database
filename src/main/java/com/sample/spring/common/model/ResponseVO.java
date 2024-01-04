package com.sample.spring.common.model;

import com.sample.spring.common.util.ContextUtil;
import org.apache.commons.lang3.StringUtils;

public class ResponseVO<T> {

    private String status;
    private String result;
//    private ErrorVo error;
    private String traceId;
    private T data;
    public String getTraceId() {
        if (StringUtils.isEmpty(traceId))
            ContextUtil.getTraceContext().ifPresent(v -> traceId = v.getTraceId());
        return traceId;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public T getData() {
        return data;
    }

//    public ErrorVo getError() {
//        return error;
//    }
//
//    public void setError(ErrorVo error) {
//        this.error = error;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(T data) {
        this.data = data;
    }

}
