package com.sample.spring.repository;


import com.sample.spring.entity.AuditTrailEntity;

public interface AuditTrailRepository {

    void save(AuditTrailEntity entity);
}
