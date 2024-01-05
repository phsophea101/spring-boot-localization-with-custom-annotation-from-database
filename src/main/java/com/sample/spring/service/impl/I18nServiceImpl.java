package com.sample.spring.service.impl;

import com.sample.spring.entity.I18nEntity;
import com.sample.spring.jackson.I18NProvider;
import com.sample.spring.repository.I18nRepository;
import com.sample.spring.service.I18nService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class I18nServiceImpl implements I18nService, I18NProvider {
    private final I18nRepository repository;

    @Override
    public String getMessage(String key, String type, String locale, String defaultMessage) {
        I18nEntity entity = new I18nEntity();
        entity.setKey(key);
        entity.setType(type);
        entity.setLocale(locale);
        entity.setStatus("ACTIVE");
        Optional<I18nEntity> nEntity = this.repository.findOne(Example.of(entity));
        if (nEntity.isPresent())
           return nEntity.get().getMessage();
        return defaultMessage;
    }
}
