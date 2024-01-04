package com.sample.spring.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SystemType {

    STATUS("status"),
    PATH("path"),
    ERROR("error"),
    FAILED("failed"),
    CODE("code"),
    DESCRIPTION("description"),
    RESULT("result"),
    EXCEPTION("exception"),
    TRACE_ID("trace_id"),
    MESSAGE("message");

    public static Boolean contains(String s) {
        for (SystemType c : SystemType.values()) {
            if (c.name().equals(s)) {
                return true;
            }
        }
        return false;
    }

    private final String value;


    public String getValue() {
        return value;
    }
}
