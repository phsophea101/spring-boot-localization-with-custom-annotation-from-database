package com.sample.spring.common.exception;

import com.sample.spring.common.consts.ErrorCodeType;
import com.sample.spring.enums.BizErrorCode;

public class BizException extends Exception {

    private static final long serialVersionUID = 4127513561428645333L;

    private ErrorCodeType error;
    private String description;

    public BizException() {
        this(BizErrorCode.E0001);
    }

    public BizException(String description) {
        super(description);
        error = BizErrorCode.E0001;
        this.description = description;
    }

    public BizException(ErrorCodeType bizError) {
        super(bizError.getDescription());
        this.error = bizError;
    }

    public BizException(ErrorCodeType bizError, String description) {
        super(description);

        this.error = bizError;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public ErrorCodeType getError() {
        return error;
    }


}
