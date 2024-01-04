package com.sample.spring.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Locale;

@Configuration
@EntityScan(basePackages = "com.sample.spring.entity")
public class AppConfiguration {
    @Bean
    public DecimalFormat decimalFormat() {
        return new DecimalFormat("#.00");
    }

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder mapperBuilder) {
        ObjectMapper mapper = mapperBuilder.build();
        mapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
        SerializerProvider serializerProvider = mapper.getSerializerProvider();
        mapper.setSerializerProvider(new DefaultSerializerProvider());
        return mapper;
    }


    @Bean
    public LocaleResolver sessionLocaleResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("en"));
        localeResolver.setSupportedLocales(Arrays.asList(new Locale("km"), new Locale("en"), new Locale("kr")));
        return localeResolver;
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