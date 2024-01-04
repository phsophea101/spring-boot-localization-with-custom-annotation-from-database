package com.sample.spring.common.concurrent;

import com.sample.spring.common.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class InheritableContextHolder {


    private static final InheritableThreadLocal<Map<String, Object>> THREAD_LOCAL = new InheritableThreadLocal() {
        @Override
        protected Map<String, Object> initialValue() {
            return new ConcurrentHashMap<>();
        }
    };

    private InheritableContextHolder() {
    }

    public static void setObject(final String key, final Object value) {
        if (StringUtils.isEmpty(key) || Objects.isNull(value)) {
            log.debug("Key {} or value {} is empty cannot put into inheritable thread local", key, value);
            return;
        }
        THREAD_LOCAL.get().put(key, value);
    }

    public static Object getObject(final String key) {
        return THREAD_LOCAL.get().get(key);
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }

    public static Object remove(final String key) {
        return THREAD_LOCAL.get().remove(key);
    }

    public static <T> T getObject(final String key, final Class<T> _class) {
        return ObjectUtils.cast(getObject(key), _class);
    }

    public static <T> T getObject(final String key, final TypeReference<T> valueTypeRef) {
        return ObjectUtils.cast(getObject(key), valueTypeRef);
    }

    public static void setString(final String key, final String value) {
        setObject(key, value);
    }

    public static String getString(final String key) {
        return getObject(key, String.class);
    }

    public static void setDate(final String key, final Date value) {
        setObject(key, value);
    }

    public static Date getDate(final String key) {
        return getObject(key, Date.class);
    }

    public static void setInteger(final String key, final Integer value) {
        setObject(key, value);
    }

    public static Integer getInteger(final String key) {
        return getObject(key, Integer.class);
    }

    public static void setLong(final String key, final Long value) {
        setObject(key, value);
    }

    public static Long getLong(final String key) {
        return getObject(key, Long.class);
    }

    public static void setFloat(final String key, final Float value) {
        setObject(key, value);
    }

    public static Float getFloat(final String key) {
        return getObject(key, Float.class);
    }

    public static void setDouble(final String key, final Double value) {
        setObject(key, value);
    }

    public static Double getDouble(final String key) {
        return getObject(key, Double.class);
    }

    public static void setBigDecimal(final String key, final BigDecimal value) {
        setObject(key, value);
    }

    public static BigDecimal getBigDecimal(final String key) {
        return getObject(key, BigDecimal.class);
    }

    public static void setBoolean(final String key, final Boolean value) {
        setObject(key, value);
    }

    public static Boolean getBoolean(final String key) {
        return getObject(key, Boolean.class);
    }

    public static boolean containsKey(String key) {
        return THREAD_LOCAL.get().containsKey(key);
    }
}