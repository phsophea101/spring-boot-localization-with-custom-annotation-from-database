package com.sample.spring.enums;

import com.sample.spring.common.consts.ErrorCodeType;

public enum BizErrorCode implements ErrorCodeType {

    /**
     * Error General exception.
     */
    E0001("E0001", "General exception error."),
    E0002("E0002", "Record not found."),
    E0003("E0003", "Record already existed."),
    E0004("E0004", "Username required."),
    E0005("E0005", "Client id not found"),
    E0006("E0006", "password required."),
    E0007("E0007", "Incorrect username or password"),
    E0008("E0008","User insufficient role"),
    S0001("S0001", "No more sequence for this transaction.");

    final String value;
    final String description;

    BizErrorCode(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }

}
