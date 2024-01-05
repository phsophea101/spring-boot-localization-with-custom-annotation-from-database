package com.sample.spring.jackson;

public interface I18NProvider {
    String getMessage(String key, String type, String locale, String defaultMessage);
}
