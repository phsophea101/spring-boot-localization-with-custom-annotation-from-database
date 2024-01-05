package com.sample.spring.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = UserEntity.TABLE_NAME)
@FieldNameConstants
public class UserEntity {
    public static final String TABLE_NAME = "users";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String gender;
    private String username;
    private String password;
    private String status;
    private boolean locked;
    @Column(name = "created_by")
    protected String createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    protected Date createdDate;
    @Column(name = "updated_by")
    protected String updatedBy;
    @Column(name = "updated_date")
    protected Date updatedDate;
}
