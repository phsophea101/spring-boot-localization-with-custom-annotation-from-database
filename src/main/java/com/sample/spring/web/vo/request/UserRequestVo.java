package com.sample.spring.web.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class UserRequestVo {
    @NotBlank
    private String username;
    @NotEmpty
    @NotBlank
    @NotNull
    private String password;
    @NotEmpty
    @NotBlank
    @NotNull
    private String createData;
    private String status;
}
