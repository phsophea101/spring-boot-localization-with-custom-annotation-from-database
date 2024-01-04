package com.sample.spring.repository.impl;

import com.sample.spring.common.model.*;
import com.sample.spring.entity.UserEntity;
import com.sample.spring.enums.StatusType;
import com.sample.spring.repository.BasicMongoRepository;
import com.sample.spring.repository.UserRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl extends BasicMongoRepository<UserEntity> implements UserRepository {
    @Autowired
    protected UserRepositoryImpl(MongoTemplate template) {
        super(template);
    }

    @Override
    public UserEntity findByUsername(String username) {
        if (ObjectUtils.isEmpty(username))
            return null;
        List<QueryModelDto> queries = new ArrayList<>();
        queries.add(new QueryModelDto(UserEntity.Fields.status, QueryCondition.IS, String.valueOf(StatusType.ACTIVE)));
        queries.add(new QueryModelDto(UserEntity.Fields.username, QueryCondition.IS, username));
        return this.findByField(new ModelQueryDto(queries, List.of(UserEntity.Fields.status, UserEntity.Fields.username)));
    }

    @Override
    public List<UserEntity> findByUsers() {
        return this.findMany(new ModelQueryDto(List.of(new QueryModelDto("status", QueryCondition.IS, "ACTIVE")), new PageableDto(Sort.by(Sort.Direction.ASC, "username"))));
    }

    @Override
    public UserEntity findOneById(String id) {
        if (ObjectUtils.isEmpty(id))
            return null;
        return this.findById(id);
    }

    @Override
    public void save(UserEntity entity) {
        this.create(entity);
    }

}
