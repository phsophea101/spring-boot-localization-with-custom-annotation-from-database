package com.sample.spring.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatusType {

    PENDING("PENDING"),
    DRAFT("DRAFT"),
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    DELETED("DELETED");

    public static Boolean contains(String s) {
        for (StatusType c : StatusType.values()) {
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
