package com.sample.spring.common.sequence.service.impl;

import com.google.common.base.CaseFormat;
import com.sample.spring.common.exception.BizException;
import com.sample.spring.common.model.ModelQueryDto;
import com.sample.spring.common.model.QueryCondition;
import com.sample.spring.common.model.QueryModelDto;
import com.sample.spring.common.sequence.SequenceType;
import com.sample.spring.common.sequence.annotation.InjectSequenceValue;
import com.sample.spring.entity.AuditTrailEntity;
import com.sample.spring.entity.MongoSequenceEntity;
import com.sample.spring.enums.BizErrorCode;
import com.sample.spring.repository.impl.SequenceRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@ConditionalOnClass(value = {AbstractMongoEventListener.class, MongoTemplate.class})
public class MongoSequenceServiceImpl extends AbstractSequenceService {
    private static final List<String> COLLECTION_IGNORES = List.of(StringUtils.EMPTY, MongoSequenceEntity.COLLECTION_NAME, AuditTrailEntity.TABLE_NAME);
    protected final SequenceRepositoryImpl repository;

    private List<Field> getAllFields(Class<?> clazz) {
        Set<Field> fields = new HashSet<>();
        Class<?> current = clazz;
        while (ObjectUtils.isNotEmpty(current) && !("java.lang.Object".equalsIgnoreCase(current.getName()))) {
            List<Field> auditFields = Arrays.stream(current.getDeclaredFields()).collect(Collectors.toList());
            if (!auditFields.isEmpty()) {
                fields.addAll(auditFields);
            } else {
                fields.addAll(Arrays.asList(current.getDeclaredFields()));
            }
            current = current.getSuperclass();
        }
        return new ArrayList<>(fields);
    }

    @Override
    public String injectFieldWithSequenceValue(Object entity, String fieldName) {
        Optional<Document> optionalDocument = Optional.ofNullable(entity.getClass().getAnnotation(Document.class));
        if (optionalDocument.isEmpty())
            return null;
        String tableName = ObjectUtils.isNotEmpty(optionalDocument.get().value()) ? optionalDocument.get().value() : optionalDocument.get().collection();
        if (COLLECTION_IGNORES.contains(tableName))
            return null;
        try {
            Optional<Field> optionalField = this.getAllFields(entity.getClass()).stream().filter(i -> i.getName().equalsIgnoreCase(fieldName)).findFirst();
            if (optionalField.isEmpty())
                return null;
            Field field = optionalField.get();//entity.getClass().getDeclaredField(fieldName);
            if (ObjectUtils.isEmpty(FieldUtils.readField(field, entity, true)) && field.isAnnotationPresent(InjectSequenceValue.class)) {
                String fieldSeq = fieldName;
                if (fieldName.startsWith("_"))
                    fieldSeq = fieldName.substring(1);
                String sequenceName = String.format("%s_%s_seq", tableName, CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldSeq)).toLowerCase();
                String sequencePrefix = field.getAnnotation(InjectSequenceValue.class).sequencePrefix();
                int sequenceDigit = field.getAnnotation(InjectSequenceValue.class).sequenceDigit();
                SequenceType sequenceType = field.getAnnotation(InjectSequenceValue.class).sequenceType();
                if (field.isAnnotationPresent(Id.class))
                    return this.generate(16, sequenceName, StringUtils.EMPTY, sequenceType, Boolean.FALSE);
                return this.generate(sequenceDigit, sequenceName, sequencePrefix, sequenceType, Boolean.TRUE);
            } else {
                return String.valueOf(FieldUtils.readField(field, entity, true));
            }
        } catch (IllegalAccessException e) {
            log.error("An error occurred from generate sequence id.", e);
        }
        return null;
    }

    @SneakyThrows
    @Override
    public Long getCurrentSequence(String sequenceName, SequenceType sequenceType, boolean recycle) {
        List<QueryModelDto> queryModels = List.of(new QueryModelDto(MongoSequenceEntity.Fields.seq, QueryCondition.IS, sequenceName));
        ModelQueryDto modelQuery = new ModelQueryDto(queryModels);
        MongoSequenceEntity entity = this.repository.findByField(modelQuery);
        if (ObjectUtils.isEmpty(entity)) {
            entity = new MongoSequenceEntity();
            entity.setSeq(sequenceName);
            entity.setCreatedDate(new Date());
            entity.setRecycle(recycle);
            entity.setIncrement(1L);
            entity.setCurrent(0L);
            entity.setMax(999999999999999999L);
        } else if (entity.getCurrent().compareTo(entity.getMax()) >= 0 && Boolean.TRUE.equals(entity.getRecycle())) {
            entity.setCurrent(0L);
        } else if (entity.getCurrent().compareTo(entity.getMax()) >= 0 && Boolean.FALSE.equals(entity.getRecycle())) {
            throw new BizException(BizErrorCode.S0001);
        }
        entity.setCurrent(entity.getCurrent() + entity.getIncrement());
        this.repository.create(entity);
        return entity.getCurrent();
    }
}
