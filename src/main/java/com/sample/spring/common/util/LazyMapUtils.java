package com.sample.spring.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LazyMapUtils extends HashMap<String, Object> {

    public LazyMapUtils() {
    }

    public LazyMapUtils(Map<? extends String, ?> m) {
        super(m);
    }

    public LazyMapUtils(String key, Object value) {
        super.put(key, value);
    }

    public LazyMapUtils append(String key, Object value) {
        super.put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key) {
        return (T) get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> optValue(String key) {
        return (Optional<T>) Optional.ofNullable(get(key));
    }

    @SuppressWarnings("unchecked")
    public <T> T optValue(String key, T defaultValue) {
        T value = (T) get(key);
        return value == null ? defaultValue : value;
    }
}
