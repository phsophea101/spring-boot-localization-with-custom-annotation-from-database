package com.sample.spring.audit.service;

import com.sample.spring.audit.dto.AuditTrailDto;
import com.sample.spring.entity.AuditTrailEntity;
import com.sample.spring.mapper.AuditTrailMapper;
import com.sample.spring.repository.impl.AuditTrailRepositoryImpl;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class AuditTrailProducerService {
    private final AuditTrailRepositoryImpl repository;

    @SneakyThrows
    public void sendMessage(AuditTrailDto audit) {
        AuditTrailEntity entity = AuditTrailMapper.INSTANCE.dtoToEntity(audit);
        this.repository.create(entity);
        log.info("Audit message send => {}", entity);
    }

}
