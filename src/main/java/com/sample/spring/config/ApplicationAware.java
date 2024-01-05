package com.sample.spring.config;

import com.sample.spring.util.ContextUtil;
import com.sample.spring.util.I18nUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;

@Slf4j
public class ApplicationAware implements ApplicationContextAware, ApplicationReadyListener {
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        log.debug("initial application context to ContextUtil");
        ContextUtil.setContext(context);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ContextUtil.optBean(MessageSource.class).ifPresent(I18nUtils::setBundleMessage);
    }
}
