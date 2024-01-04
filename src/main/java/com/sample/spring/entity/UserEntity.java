package com.sample.spring.entity;

import com.sample.spring.common.model.AuditEntity;
import com.sample.spring.common.model.RecyclableEntity;
import com.sample.spring.common.sequence.annotation.InjectSequenceValue;
import com.sample.spring.enums.StatusType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Setter
@Getter
@Document(collection = UserEntity.TABLE_NAME)
@FieldNameConstants
public class UserEntity extends AuditEntity implements RecyclableEntity<String> {
    public static final String TABLE_NAME = "users";
    @Indexed(unique = true)
    private String username;
    @Indexed
    private String password;
    private List<RoleEntity> roles;
    @Indexed
    private String status;
    @InjectSequenceValue(sequencePrefix = "URS-")
    @Field("user_code")
    private String userCode;
    private boolean locked;

}
