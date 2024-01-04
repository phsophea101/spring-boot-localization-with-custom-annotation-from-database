package com.sample.spring.audit.service;

import com.sample.spring.audit.dto.AuditTrailDto;
import com.sample.spring.audit.dto.AuditTrailPrimaryKeyDto;
import com.sample.spring.common.util.ClassUtil;
import com.sample.spring.common.util.StringUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface InterfaceAuditTrailService {
    void sendMessage(AuditTrailDto table);

    boolean isDeleted(Object entity);

    boolean isAudited(Class<?> clazz);

    List<Field> getPrimaryKeyFields(Object entity);

    List<Field> getAllFields(Class<?> type);

    Field[] getAllFields(Class<?> clazz, Field[] fields);

    List<AuditTrailPrimaryKeyDto> getPrimaryKeyValues(Object object);

    String getTableName(Class<?> clazz);

    String getIdentityKey(Object object);

    AuditTrailDto getCreatedData(Object object);

    default boolean isModified(Object currentValue, Object previousValue) {
        return Objects.isNull(currentValue) ? !Objects.isNull(previousValue) : ClassUtil.isJavaLangType(previousValue) && !currentValue.equals(previousValue);
    }

    default String caseFieldName(String fieldName) {
        return StringUtil.camelCaseToSnakeCase(fieldName);
    }

    default String getUserFullName() {
        return "ANONYMOUS";
    }

    default List<String> getRoleNames() {
        return List.of("ADMIN");
    }

    default List<String> getRoleIds() {
        return List.of("1000");
    }

    default String getApplicationName() {
        return Optional.ofNullable(System.getenv("NAME_SPACE")).orElse("LOCALLY");
    }

}
