package com.sample.spring.service.impl;

import com.sample.spring.common.exception.BizException;
import com.sample.spring.dto.UserDto;
import com.sample.spring.entity.UserEntity;
import com.sample.spring.enums.BizErrorCode;
import com.sample.spring.enums.StatusType;
import com.sample.spring.mapper.UserMapper;
import com.sample.spring.repository.UserRepository;
import com.sample.spring.service.UserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserService {
    private final UserRepository repository;

    @SneakyThrows
    @Override
    public UserDto loadUserByUsername(String username)  {
        UserEntity entity = this.repository.findByUsername(username);
        if (ObjectUtils.isEmpty(entity))
            throw new BizException(BizErrorCode.E0002);
        return UserMapper.INSTANCE.entityToDto(entity);
    }

    @SneakyThrows
    @Override
    public UserDto save(UserDto dto) {
        UserEntity entity = this.repository.findByUsername(dto.getUsername());
        if (ObjectUtils.isNotEmpty(entity))
            throw new BizException(BizErrorCode.E0003, String.format("This username %s %s", dto.getUsername(), "already existed."));
        entity = UserMapper.INSTANCE.dtoToEntity(dto);
        entity.setStatus(String.valueOf(StatusType.ACTIVE));
        this.repository.save(entity);
        return UserMapper.INSTANCE.entityToDto(entity);
    }

    @Override
    public List<UserDto> findByUsers() {
        return UserMapper.INSTANCE.entityToDtoList(repository.findByUsers());
    }

    @SneakyThrows
    @Override
    public UserDto findById(String id) {
        UserEntity entity = this.repository.findOneById(id);
        if (ObjectUtils.isEmpty(entity))
            throw new BizException(BizErrorCode.E0002);
        return UserMapper.INSTANCE.entityToDto(entity);
    }
}
