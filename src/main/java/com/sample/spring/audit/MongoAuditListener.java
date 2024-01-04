package com.sample.spring.audit;

import com.sample.spring.audit.conts.ActionStatus;
import com.sample.spring.audit.dto.AuditTrailDto;
import com.sample.spring.audit.dto.AuditTrailFieldDto;
import com.sample.spring.audit.service.impl.MongoAuditTrailServiceImpl;
import com.sample.spring.common.concurrent.InheritableContextHolder;
import com.sample.spring.common.util.ContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.Document;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@ConditionalOnClass(value = {AbstractMongoEventListener.class, MongoTemplate.class})
public class MongoAuditListener<T> {
    private static final String ERROR_MESSAGE = "Error occur audit {}";

    @EventListener
    public void onAfterConvert(AfterConvertEvent<T> event) {
        try {
            Optional<MongoAuditTrailServiceImpl> auditTrailServiceOptional = ContextUtil.optBean(MongoAuditTrailServiceImpl.class);
            if (auditTrailServiceOptional.isPresent()) {
                MongoAuditTrailServiceImpl auditTrailService = auditTrailServiceOptional.get();
                Document source = event.getDocument();
                if (Boolean.TRUE.equals(auditTrailService.isAudited(event.getSource().getClass())) && Objects.nonNull(source)) {
                    String identityKey = auditTrailService.getIdentityKey(event.getSource());
                    InheritableContextHolder.setObject(identityKey, source);
                }
            }
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e);
        }
    }

    @EventListener
    public void onAfterSave(AfterSaveEvent<T> event) {
        try {
            Optional<MongoAuditTrailServiceImpl> auditTrailServiceOptional = ContextUtil.optBean(MongoAuditTrailServiceImpl.class);
            if (auditTrailServiceOptional.isPresent()) {
                Document newSource = event.getDocument();
                if (Objects.nonNull(newSource)) {
                    String identityKey = auditTrailServiceOptional.get().getIdentityKey(event.getSource());
                    Document oldSource = InheritableContextHolder.getObject(identityKey, Document.class);
                    doAudit(oldSource, event);
                    InheritableContextHolder.remove(identityKey);
                }
            }
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e);
        }
    }

    @EventListener
    public void onAfterDelete(AfterDeleteEvent<T> event) {
        try {
            Optional<MongoAuditTrailServiceImpl> auditTrailServiceOptional = ContextUtil.optBean(MongoAuditTrailServiceImpl.class);
            if (auditTrailServiceOptional.isPresent()) {
                MongoAuditTrailServiceImpl auditTrailService = auditTrailServiceOptional.get();
                if (Boolean.TRUE.equals(auditTrailService.isAudited(event.getSource().getClass()))) {
                    AuditTrailDto auditTableDto = auditTrailService.getCreatedData(event.getSource());
                    if ("NA".equalsIgnoreCase(auditTableDto.getTableName()))
                        auditTableDto.setTableName(event.getCollectionName());
                    auditTableDto.setAction(String.valueOf(ActionStatus.DELETED));
                    auditTrailService.sendMessage(auditTableDto);
                }
            }
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e);
        }
    }

    private void doAudit(Document oldSource, AfterSaveEvent<T> event) {
        Document newSource = event.getDocument();
        T source = event.getSource();
        try {
            Optional<MongoAuditTrailServiceImpl> auditTrailServiceOptional = ContextUtil.optBean(MongoAuditTrailServiceImpl.class);
            if (auditTrailServiceOptional.isPresent()) {
                MongoAuditTrailServiceImpl auditTrailService = auditTrailServiceOptional.get();
                if (Boolean.TRUE.equals(auditTrailService.isAudited(source.getClass())) && Objects.nonNull(newSource)) {
                    AuditTrailDto auditTableDto = auditTrailService.getCreatedData(source);
                    if ("NA".equalsIgnoreCase(auditTableDto.getTableName()))
                        auditTableDto.setTableName(event.getCollectionName());
                    if (Objects.nonNull(oldSource)) {
                        List<AuditTrailFieldDto> changedFields = auditTrailService.getChangedFields(source.getClass(), oldSource, newSource);
                        if (ObjectUtils.isNotEmpty(changedFields)) {
                            auditTableDto.setFields(changedFields);
                            if (!String.valueOf(ActionStatus.DELETED).equalsIgnoreCase(auditTableDto.getAction()))
                                auditTableDto.setAction(String.valueOf(ActionStatus.MODIFIED));
                            auditTrailService.sendMessage(auditTableDto);
                        }
                    } else {
                        auditTrailService.sendMessage(auditTableDto);
                    }
                }
            }
        } catch (Exception e) {
            log.error(ERROR_MESSAGE, e.getMessage(), e);
        }
    }
}