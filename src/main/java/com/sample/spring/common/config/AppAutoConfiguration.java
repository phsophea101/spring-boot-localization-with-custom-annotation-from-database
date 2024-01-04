package com.sample.spring.common.config;

import com.sample.spring.common.util.ContextUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(value = {ApplicationContext.class, ContextUtil.class})
public class AppAutoConfiguration {
    @Bean("applicationAware")
    @ConditionalOnMissingBean(ApplicationAware.class)
    public ApplicationAware applicationAware() {
        return new ApplicationAware();
    }
}