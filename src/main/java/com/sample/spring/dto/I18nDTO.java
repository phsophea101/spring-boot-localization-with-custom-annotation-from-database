package com.sample.spring.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class I18nDTO implements Serializable {
    private static final long serialVersionUID = 1905122041950251207L;
    private String id;
    private String key;
    private String locale;
    private String message;
    private String type;
}