package com.sample.spring.service;

import com.sample.spring.dto.UserDto;

import java.util.List;

public interface UserService /*extends UserDetailsService*/ {
    UserDto loadUserByUsername(String username);

    UserDto save(UserDto dto);

    List<UserDto> findByUsers();

    UserDto findById(String id);
}
