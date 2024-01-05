package com.sample.spring.web.vo.response;

import com.sample.spring.conts.BizErrorCode;
import com.sample.spring.util.I18nUtils;

public class ResponseVOBuilder<T> {
    private final ResponseVO<T> responseVO = new ResponseVO<>();

    private ResponseVOBuilder<T> result(String result) {
        responseVO.setResult(result);
        return this;
    }

    private ResponseVOBuilder<T> status(String status) {
        responseVO.setStatus(status);
        return this;
    }

    public ResponseVOBuilder<T> success() {
        return new ResponseVOBuilder<T>().result(I18nUtils.messageResolver("SUCCEEDED", "Succeeded")).status("200");
    }

    public ResponseVOBuilder<T> fail() {
        return new ResponseVOBuilder<T>().result(I18nUtils.messageResolver("FAILED", "Failed")).status("400");
    }

    public ResponseVOBuilder<T> error(ResponseErrorVo error) {
        String message = "Failed";
        if (!error.getCode().equalsIgnoreCase(BizErrorCode.E0001.getValue()))
            message = I18nUtils.messageResolver("FAILED", "Failed");
        responseVO.setError(error);
        responseVO.setResult(message);
        responseVO.setStatus("400");
        return this;
    }

    public ResponseVOBuilder<T> addData(final T body) {
        responseVO.setData(body);
        responseVO.setResult(I18nUtils.messageResolver("SUCCEEDED", "Succeeded"));
        responseVO.setStatus("200");
        return this;
    }

    public ResponseVO<T> build() {
        return responseVO;
    }
}
