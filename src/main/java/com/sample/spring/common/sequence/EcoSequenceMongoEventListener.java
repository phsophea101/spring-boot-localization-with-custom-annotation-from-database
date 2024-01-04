package com.sample.spring.common.sequence;

import com.google.common.base.CaseFormat;
import com.sample.spring.common.sequence.annotation.InjectSequenceValue;
import com.sample.spring.common.sequence.service.SequenceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Slf4j
@Component
@ConditionalOnClass(value = {AbstractMongoEventListener.class, MongoTemplate.class})
public class EcoSequenceMongoEventListener<E> extends AbstractMongoEventListener<E> {
    @Autowired
    private SequenceService service;

    @Override
    public void onBeforeSave(BeforeSaveEvent<E> event) {
        try {
            this.setSequenceFields(event);
        } catch (Exception e) {
            log.error("Error occurred on generate sequence.", e);
        } finally {
            super.onBeforeSave(event);
        }
    }

    @SneakyThrows
    private void setSequenceFields(BeforeSaveEvent<E> event) {
        Object entity = event.getSource();
        Class<?> current = entity.getClass();
        while (ObjectUtils.isNotEmpty(current) && !current.getName().equals("java.lang.Object")) {
            for (Field field : current.getDeclaredFields()) {
                if (ObjectUtils.isEmpty(FieldUtils.readField(field, entity, true)) && field.isAnnotationPresent(InjectSequenceValue.class)) {
                    String sequenceFormat = this.service.injectFieldWithSequenceValue(entity, field.getName());
                    if (ObjectUtils.isNotEmpty(sequenceFormat))
                        if (field.isAnnotationPresent(Id.class))
                            event.getDocument().append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()), new ObjectId(sequenceFormat));
                        else
                            event.getDocument().append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()), sequenceFormat);
                }
            }
            current = current.getSuperclass();
        }
    }
}
