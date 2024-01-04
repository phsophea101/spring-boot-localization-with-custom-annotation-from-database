package com.sample.spring.audit.service.impl;

import com.sample.spring.audit.conts.ActionStatus;
import com.sample.spring.audit.dto.AuditTrailDto;
import com.sample.spring.audit.dto.AuditTrailFieldDto;
import com.sample.spring.audit.dto.AuditTrailPrimaryKeyDto;
import com.sample.spring.audit.dto.RoleDto;
import com.sample.spring.audit.service.AuditTrailProducerService;
import com.sample.spring.audit.service.InterfaceAuditTrailService;
import com.sample.spring.common.TraceContext;
import com.sample.spring.common.util.ClassUtil;
import com.sample.spring.common.util.ContextUtil;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public abstract class AbstractAuditTrailService implements InterfaceAuditTrailService {
    public static final List<String> FIELD_IGNORES = List.of("created_by", "created_date", "updated_by", "updated_date", "createdBy", "createdDate", "updatedBy", "updatedDate", "updatedDateTime", "class", "hibernateLazyInitializer");
    public static final List<String> TABLE_IGNORES = List.of("audit_trails", "oauth_access_token", "oauth_refresh_token");
    public static final String AUDIT_TRAIL_TOPIC_VALUES = "AUDIT_TRAIL_TOPICS";
    public static final String AUDIT_TRAIL_TOPIC_KEYS = "audit.trail.topic.keys";
    public static final String AUDIT_TRAIL_TABLE_KEYS = "audit.trail.table.keys";
    private static final String ALL = "*";
    private static final String PROFILE_LOCAL = "local";
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${spring.profiles.active}")
    private String profileActive;

    @Override
    public String getApplicationName() {
        return Optional.ofNullable(this.applicationName).orElse(InterfaceAuditTrailService.super.getApplicationName());
    }

    @Override
    public void sendMessage(AuditTrailDto table) {
        table.setTraceId(StringUtils.EMPTY);
        Optional<TraceContext> traceContext = ContextUtil.getTraceContext();
        traceContext.ifPresent(context -> table.setTraceId(context.getTraceId()));
        table.setUser(this.getUserFullName());
        table.setCreatedBy(Optional.ofNullable("login_id").orElse("ANONYMOUS"));
        table.setCreatedDate(new Date());
        table.setServiceName(this.getApplicationName());
        Optional<AuditTrailProducerService> producer = ContextUtil.optBean(AuditTrailProducerService.class);
        producer.ifPresent(auditTrailProducerService -> auditTrailProducerService.sendMessage(table));
    }

    @Override
    public String getIdentityKey(Object object) {
        return String.format("%s_(%s)", this.getTableName(object.getClass()).toUpperCase(), org.springframework.util.ObjectUtils.getIdentityHexString(object));
    }

    @Override
    public Field[] getAllFields(Class<?> clazz, Field[] fields) {
        Field[] newFields = clazz.getDeclaredFields();
        int fieldsSize = 0;
        int newFieldsSize = 0;
        if (fields != null)
            fieldsSize = fields.length;
        if (newFields != null)
            newFieldsSize = newFields.length;
        Field[] totalFields = new Field[fieldsSize + newFieldsSize];
        if (fieldsSize > 0)
            System.arraycopy(fields, 0, totalFields, 0, fieldsSize);
        if (newFieldsSize > 0)
            System.arraycopy(newFields, 0, totalFields, fieldsSize, newFieldsSize);
        Class<?> superClass = clazz.getSuperclass();
        Field[] finalFieldsArray;
        if (superClass != null && !("java.lang.Object".equalsIgnoreCase(superClass.getName()))) {
            finalFieldsArray = getAllFields(superClass, totalFields);
        } else {
            finalFieldsArray = totalFields;
        }
        return finalFieldsArray;
    }

    @Override
    public List<Field> getAllFields(Class<?> clazz) {
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
    public boolean isDeleted(Object object) {
        List<Field> fields = getAllFields(object.getClass());
        for (Field field : fields) {
            String fieldName = field.getName();
            if (fieldName.equalsIgnoreCase("status") && List.of(String.valueOf(ActionStatus.DELETED), "DELETE").contains(String.valueOf(new BeanMap(object).get(fieldName)))) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public boolean isAudited(Class<?> clazz) {
        if (PROFILE_LOCAL.equalsIgnoreCase(profileActive))
            return Boolean.FALSE;
        String tableName = this.getTableName(clazz);
        if (TABLE_IGNORES.contains(tableName))
            return Boolean.FALSE;
        String[] tables = {"*"};
        for (String table : tables) {
            if (tableName.equalsIgnoreCase(table) || ALL.equals(table))
                return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public List<AuditTrailPrimaryKeyDto> getPrimaryKeyValues(Object object) {
        List<Field> primaryKeyFields = this.getPrimaryKeyFields(object);
        List<AuditTrailPrimaryKeyDto> primaryKeyValues = new ArrayList<>();
        for (Object propNameObject : new BeanMap(object).keySet()) {
            String fieldName = (String) propNameObject;
            Object value = new BeanMap(object).get(fieldName);
            primaryKeyFields.forEach(item -> {
                if (item.getName().equalsIgnoreCase(fieldName))
                    primaryKeyValues.add(new AuditTrailPrimaryKeyDto(this.caseFieldName(fieldName), String.valueOf(value)));
            });
        }
        return primaryKeyValues;
    }

    @Override
    public AuditTrailDto getCreatedData(Object object) {
        AuditTrailDto table = new AuditTrailDto();
        if (ObjectUtils.anyNotNull(this.getRoleIds(), this.getRoleNames())) {
            List<RoleDto> roles = new ArrayList<>();
            for (String roleId : this.getRoleIds()) {
                for (String roleName : this.getRoleNames()) {
                    roles.add(new RoleDto(roleId, roleName));
                }
            }
            table.setRoles(roles);
        }
        List<AuditTrailFieldDto> fields = new ArrayList<>();
        List<AuditTrailPrimaryKeyDto> primaryKeyValues = this.getPrimaryKeyValues(object);
        List<String> primaryKeyFields = primaryKeyValues.stream().map(AuditTrailPrimaryKeyDto::getFieldName).collect(Collectors.toList());
        AtomicReference<BeanMap> map = new AtomicReference<>(new BeanMap(object));
        map.get().keySet().stream().map(String.class::cast).forEach(fieldName -> {
            Object currentValue = map.get().get(fieldName);
            if (!FIELD_IGNORES.contains(fieldName) && ObjectUtils.isNotEmpty(currentValue) && ClassUtil.isJavaLangType(currentValue.getClass()) && !primaryKeyFields.contains(this.caseFieldName(fieldName)))
                fields.add(new AuditTrailFieldDto(this.caseFieldName(fieldName), String.valueOf(currentValue), StringUtils.EMPTY));
        });
        table.setTableName(this.getTableName(object.getClass()));
        table.setFields(fields);
        table.setPrimaryKeys(primaryKeyValues);
        if (this.isDeleted(object))
            table.setAction(String.valueOf(ActionStatus.DELETED));
        else
            table.setAction(String.valueOf(ActionStatus.CREATED));
        return table;
    }
}
