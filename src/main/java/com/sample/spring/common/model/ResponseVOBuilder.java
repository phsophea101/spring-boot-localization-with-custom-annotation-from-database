package com.sample.spring.common.model;

import com.sample.spring.common.util.I18nUtils;

public class ResponseVOBuilder<T> {
    private final ResponseVO<T> responseVO = new ResponseVO<>();

//    private ResponseVOBuilder<T> result(String result) {
//        responseVO.setResult(result);
//        return this;
//    }
//
//    private ResponseVOBuilder<T> status(String status) {
//        responseVO.setStatus(status);
//        return this;
//    }

//    public ResponseVOBuilder<T> success() {
//        return new ResponseVOBuilder<T>().result("Succeed").status("200");
//    }
//
//    public ResponseVOBuilder<T> fail() {
//        return new ResponseVOBuilder<T>().result("Failed");
//    }

//    public ResponseVOBuilder<T> error(ErrorVo error) {
//        responseVO.setError(error);
//        return this;
//    }

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
