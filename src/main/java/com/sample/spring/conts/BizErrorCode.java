package com.sample.spring.conts;

public enum BizErrorCode implements ErrorCodeType {
    /**
     * Error General exception.
     */
    E0000("E0000", "General exception error."),
    E0001("E0001", "Accept language header not support for [%s]."),
    E0002("E0002", "Record not found."),
    ;

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
