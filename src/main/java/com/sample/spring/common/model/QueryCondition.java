package com.sample.spring.common.model;

import com.sample.spring.enums.SystemType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum QueryCondition {
    IS("is"),
    LIKE("like"),
    ILIKE("ilike"),
    ;

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
