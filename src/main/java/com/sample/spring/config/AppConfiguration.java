package com.sample.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sample.spring.jackson.I18NModule;
import com.sample.spring.service.impl.I18nServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
public class AppConfiguration {
    @Bean
    public ObjectMapper mapper(I18nServiceImpl provider) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.failOnUnknownProperties(false);
        ObjectMapper mapper = builder.build();
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        if (ObjectUtils.isNotEmpty(serializerProvider))
            mapper.setSerializerProvider(new CustomDefaultSerializerProvider());
        mapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
        mapper.registerModule(new I18NModule(provider));
        return mapper;
    }

    @Bean
    public I18NModule i18NModule(I18nServiceImpl provider) {
        return new I18NModule(provider);
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new SmartLocaleResolver();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        /* /src/main/resources/i18ns */
        source.setBasename("i18ns/messages");
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");
        return source;
    }
}
