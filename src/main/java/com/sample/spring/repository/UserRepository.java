package com.sample.spring.repository;


import com.sample.spring.entity.UserEntity;

import java.util.List;

public interface UserRepository {

    UserEntity findByUsername(String username);
    List<UserEntity> findByUsers();

    UserEntity findOneById(String id);

    void save(UserEntity entity);
}
