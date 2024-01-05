package com.sample.spring.web.vo.response;

import com.sample.spring.jackson.annotation.I18NProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserResponseVo {
    private String id;
    private String name;
    @I18NProperty(fieldId = "gender", type = "gender",locale = "km")
    private String gender;
    private String username;
    private String status;
    private Boolean isGraduated;
    private List<String> skills;
    private Long age;

}
