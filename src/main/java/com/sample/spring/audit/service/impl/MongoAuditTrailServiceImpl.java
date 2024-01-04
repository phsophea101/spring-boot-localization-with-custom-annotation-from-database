package com.sample.spring.audit.service.impl;

import com.sample.spring.audit.dto.AuditTrailFieldDto;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
@ConditionalOnClass(value = {AbstractMongoEventListener.class, MongoTemplate.class})
public class MongoAuditTrailServiceImpl extends AbstractAuditTrailService {

    @Override
    public String getTableName(Class<?> clazz) {
        Optional<org.springframework.data.mongodb.core.mapping.Document> optionalDocument = Optional.ofNullable(clazz.getAnnotation(org.springframework.data.mongodb.core.mapping.Document.class));
        return optionalDocument.map(document -> ObjectUtils.isNotEmpty(document.collection()) ? document.collection() : document.value()).orElse("NA").replace("`", "");
    }

    @Override
    public List<Field> getPrimaryKeyFields(Object entity) {
        List<Field> primaryFields = new ArrayList<>();
        Class<?> current = entity.getClass();
        while (current != null && current != Object.class) {
            List<Field> ids = Arrays.stream(current.getDeclaredFields()).filter(field -> Objects.nonNull(field.getAnnotation(org.springframework.data.annotation.Id.class))).collect(Collectors.toList());
            primaryFields.addAll(ids);
            current = current.getSuperclass();
        }
        return primaryFields;
    }


    public List<AuditTrailFieldDto> getChangedFields(Class<?> entity, Document oldSource, Document newSource) {
        List<AuditTrailFieldDto> changedFields = new ArrayList<>();
        List<Field> fields = this.getAllFields(entity);
        for (Field field : fields) {
            org.springframework.data.mongodb.core.mapping.Field col = field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class);
            String fieldName = Objects.nonNull(col) && StringUtils.isNoneBlank(col.value()) ? col.value() : this.caseFieldName(field.getName());
            if (!FIELD_IGNORES.contains(fieldName)) {
                if (Objects.nonNull(oldSource)) {
                    Object oldValue = oldSource.get(fieldName);
                    Object newValue = newSource.get(fieldName);
                    boolean modified = this.isModified(newValue, oldValue);
                    if (modified)
                        changedFields.add(new AuditTrailFieldDto(this.caseFieldName(fieldName), String.valueOf(newValue), String.valueOf(oldValue)));
                } else {
                    Object newValue = newSource.get(fieldName);
                    changedFields.add(new AuditTrailFieldDto(fieldName, String.valueOf(newValue), StringUtils.EMPTY));
                }
            }
        }
        return changedFields;
    }
}
