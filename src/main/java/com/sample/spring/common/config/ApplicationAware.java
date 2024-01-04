package com.sample.spring.common.config;

import com.sample.spring.common.util.ContextUtil;
import com.sample.spring.common.util.I18nUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.util.ClassUtils;

import java.util.Optional;

@Slf4j
public class ApplicationAware implements ApplicationContextAware, ApplicationReadyListener {


    private static Object sleuthTracer;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        log.debug("initial application context to ContextUtil");
        ContextUtil.setContext(context);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ContextUtil.optBean(MessageSource.class).ifPresent(I18nUtils::setBundleMessage);
        loadSleuthTracer();
    }

    private void loadSleuthTracer() {
        String traceClass = "org.springframework.cloud.sleuth.Tracer";
        if (ClassUtils.isPresent(traceClass, null)) {
            log.debug("find sleuth tracer:{}", traceClass);
            try {
                Class<?> clazz = Class.forName(traceClass);
                Optional<?> optBean = ContextUtil.optBean(clazz);
                optBean.ifPresent(v -> sleuthTracer = v);
            } catch (Exception e) {
                log.warn("Cannot load {}",traceClass, e);
            }
        }
    }

    public static Optional<Object> getSleuthTracer() {
        return Optional.ofNullable(sleuthTracer);
    }
}
