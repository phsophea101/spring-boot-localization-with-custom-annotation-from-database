package com.sample.spring.web;

import com.sample.spring.common.model.ResponseVO;
import com.sample.spring.common.model.ResponseVOBuilder;
import com.sample.spring.dto.UserDto;
import com.sample.spring.mapper.UserMapper;
import com.sample.spring.service.UserService;
import com.sample.spring.web.vo.UserRequestVo;
import com.sample.spring.web.vo.UserResponseVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping("id/{id}")
    public ResponseVO<UserResponseVo> getUserById(@PathVariable String id) {
        UserDto user = this.service.findById(id);
        return new ResponseVOBuilder<UserResponseVo>().addData(UserMapper.INSTANCE.dtoToVo(user)).build();
    }

    @GetMapping("username/{username}")
    public ResponseVO<UserResponseVo> getUserByUsername(@PathVariable String username) {
        UserDto user = this.service.loadUserByUsername(username);
        return new ResponseVOBuilder<UserResponseVo>().addData(UserMapper.INSTANCE.dtoToVo(user)).build();
    }

    @GetMapping
    public ResponseVO<List<UserResponseVo>> getUsers() {
        List<UserDto> users = this.service.findByUsers();
        List<UserResponseVo> responseVo = UserMapper.INSTANCE.dtoToVoList(users);
        return new ResponseVOBuilder<List<UserResponseVo>>().addData(responseVo).build();
    }

    @PostMapping
    public ResponseVO<UserResponseVo> save(@RequestBody UserRequestVo vo) {
        UserDto dto = UserMapper.INSTANCE.voToDto(vo);
        dto = this.service.save(dto);
        UserResponseVo responseVo = UserMapper.INSTANCE.dtoToVo(dto);
        return new ResponseVOBuilder<UserResponseVo>().addData(responseVo).build();
    }

}
