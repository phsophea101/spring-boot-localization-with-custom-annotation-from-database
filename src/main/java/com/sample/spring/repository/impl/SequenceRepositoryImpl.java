package com.sample.spring.repository.impl;


import com.sample.spring.entity.MongoSequenceEntity;
import com.sample.spring.repository.BasicMongoRepository;
import com.sample.spring.repository.SequenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SequenceRepositoryImpl extends BasicMongoRepository<MongoSequenceEntity> implements SequenceRepository {
    @Autowired
    protected SequenceRepositoryImpl(MongoTemplate template) {
        super(template);
    }
}
