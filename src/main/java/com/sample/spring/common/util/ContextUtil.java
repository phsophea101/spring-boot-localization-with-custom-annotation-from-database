package com.sample.spring.common.util;

import com.sample.spring.common.config.ApplicationAware;
import com.sample.spring.common.TraceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public final class ContextUtil {

    private static final String ACTIVE_PROFILES_PROPERTY = "spring.profiles.active";
    private static ApplicationContext context;
    private static final String APPLICATION = "application-";
    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            ClassPathResource pathResource = new ClassPathResource("application.properties");
            if (pathResource.exists()) {
                PROPERTIES.load(pathResource.getInputStream());
            } else {
                pathResource = new ClassPathResource("application.yml");
                pathResource = pathResource.exists() ? pathResource : new ClassPathResource("application.yaml");
                PROPERTIES.putAll(CustomFileUtil.readYaml(pathResource));
            }
            String activeProfile = System.getProperty(ACTIVE_PROFILES_PROPERTY);
            if (StringUtils.isEmpty(activeProfile))
                activeProfile = PROPERTIES.getProperty(ACTIVE_PROFILES_PROPERTY);
            if (StringUtils.isNotEmpty(activeProfile)) {
                log.debug("active profile:{}", activeProfile);
                pathResource = new ClassPathResource(APPLICATION + activeProfile + ".properties");
                if (pathResource.exists()) {
                    PROPERTIES.putAll(new InputStreamProperties(pathResource.getInputStream()));
                } else {
                    pathResource = new ClassPathResource(APPLICATION + activeProfile + ".yml");
                    pathResource = pathResource.exists() ? pathResource : new ClassPathResource(APPLICATION + activeProfile + ".yaml");
                    PROPERTIES.putAll(CustomFileUtil.readYaml(pathResource));
                }
            }
        } catch (IOException ex) {
            log.warn("Failed to load application properties", ex);
        }
    }

    public static void setContext(ApplicationContext context) {
        log.debug("initialize application context to ContextUtil");
        ContextUtil.context = context;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static String getActiveProfile() {
        return PROPERTIES.getProperty(ACTIVE_PROFILES_PROPERTY);
    }

    public static boolean isProfile(String... profiles) {
        String activeProfile = getActiveProfile();
        for (String profile : profiles) {
            if (activeProfile.equals(profile))
                return true;
        }
        return false;
    }

    public static boolean isProfile(List<String> profiles) {
        String activeProfile = getActiveProfile();
        for (String profile : profiles) {
            if (activeProfile.equals(profile))
                return true;
        }
        return false;
    }

    public static boolean isProfileTesting() {
        List<String> profiles = List.of("dev", "local", "test", "debug");
        return isProfile(profiles);
    }

    public static <T> T getBean(Class<T> clazz) throws BeansException {
        if (!isApplicationContextActive())
            throw new IllegalStateException("Application context is not active");
        return context == null ? null : context.getBean(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) throws BeansException {
        if (!isApplicationContextActive())
            throw new IllegalStateException("Application context is not active");
        return context == null ? null : (T) context.getBean(beanName);
    }

    public static boolean isApplicationContextActive() {
        return (context instanceof ConfigurableApplicationContext
                && ((ConfigurableApplicationContext) context).isActive());
    }

    public static <T> Optional<T> optBean(Class<T> clazz) {
        try {
            return Optional.ofNullable(getBean(clazz));
        } catch (IllegalStateException | BeansException e) {
            return Optional.empty();
        }
    }

    public static <T> Optional<T> optBean(String beanName) {
        try {
            return Optional.ofNullable(getBean(beanName));
        } catch (IllegalStateException | BeansException e) {
            return Optional.empty();
        }
    }

    public static <T> Optional<T> optBean(BeanFactory factory, Class<T> clazz) {
        try {
            return Optional.of(factory.getBean(clazz));
        } catch (BeansException e) {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> optBean(BeanFactory factory, String beanName) {
        try {
            return (Optional<T>) Optional.of(factory.getBean(beanName));
        } catch (BeansException e) {
            return Optional.empty();
        }
    }

    public static boolean containProperty(String key) {
        return PROPERTIES.containsKey(key);
    }

    public static Object putProperty(Object key, Object value) {
        return PROPERTIES.put(key, value);
    }

    public static Object putPropertyIfAbsent(Object key, Object value) {
        return PROPERTIES.putIfAbsent(key, value);
    }

    public static Object removeProperty(Object key) {
        return PROPERTIES.remove(key);
    }

    public static String getProperty(String key) {
        return getProperty(key, null);
    }

    public static String getProperty(String key, String defaultValue) {
        return getProperty(key, String.class, defaultValue);
    }

    public static boolean getBoolProperty(String key, boolean defaultValue) {
        return getProperty(key, Boolean.class, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        Object value = resolve(PROPERTIES.getOrDefault(key, defaultValue));
        log.debug("accessing property {}={}", key, value);
        if (Objects.isNull(value))
            return (T) value;
        if (Boolean.class.equals(targetType))
            value = Boolean.valueOf(value.toString());
        else if (Integer.class.equals(targetType))
            value = Integer.valueOf(value.toString());
        else if (Long.class.equals(targetType))
            value = Long.valueOf(value.toString());
        else if (Double.class.equals(targetType))
            value = Double.valueOf(value.toString());
        else if (Float.class.equals(targetType))
            value = Float.valueOf(value.toString());
        else if (String.class.equals(targetType))
            value = String.valueOf(value);
        return (T) value;
    }

    public static Optional<TraceContext> getTraceContext() {
        TraceContext traceContext = new TraceContext();
        Optional<Object> sleuthTracer = ApplicationAware.getSleuthTracer();
        if (sleuthTracer.isEmpty())
            return Optional.empty();
        sleuthTracer
                .map(tracer -> ReflectionUtil.safeInvokeMethod("currentSpan", tracer)
                        .map(span -> ReflectionUtil.safeInvokeMethod("context", span)
                                .map(context -> {
                                    ReflectionUtil.safeInvokeMethod("traceId", context)
                                            .ifPresent(traceId -> traceContext.setTraceId(traceId.toString()));
                                    ReflectionUtil.safeInvokeMethod("parentId", context)
                                            .ifPresent(parentId -> traceContext.setParentId(parentId.toString()));
                                    ReflectionUtil.safeInvokeMethod("spanId", context)
                                            .ifPresent(spanId -> traceContext.setSpanId(spanId.toString()));
                                    ReflectionUtil.safeInvokeMethod("sampled", context)
                                            .ifPresent(sampled -> traceContext.setSampled(Boolean.parseBoolean(sampled.toString())));
                                    return context;
                                })));
        return Optional.of(traceContext);
    }

    private static Object resolve(Object value) {
        if (value == null) return value;
        Object result = value;
        String regex = "\\$\\{(.*?)}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result.toString());
        while (matcher.find()) {
            String[] groups = matcher.group(1).split(":", 2);
            result = System.getenv(groups[0]);
            if (result != null)
                return result;
            else if (groups.length == 2)
                return groups[1];
        }
        return value;
    }
}
