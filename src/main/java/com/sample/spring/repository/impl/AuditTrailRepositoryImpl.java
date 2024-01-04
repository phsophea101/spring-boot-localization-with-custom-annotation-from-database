package com.sample.spring.repository.impl;

import com.sample.spring.entity.AuditTrailEntity;
import com.sample.spring.repository.AuditTrailRepository;
import com.sample.spring.repository.BasicMongoRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuditTrailRepositoryImpl extends BasicMongoRepository<AuditTrailEntity> implements AuditTrailRepository {
    protected AuditTrailRepositoryImpl(MongoTemplate template) {
        super(template);
    }

    @Override
    public void save(AuditTrailEntity entity) {
        this.create(entity);
    }
}
