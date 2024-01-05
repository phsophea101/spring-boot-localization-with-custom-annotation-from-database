package com.sample.spring.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = I18nEntity.TABLE_NAME)
public class I18nEntity {
    public static final String TABLE_NAME = "i18n";
    @Id
    @Column(nullable = false, unique = true)
    private String id;
    @Column(nullable = false)
    private String key;
    @Column(nullable = false)
    private String locale;
    @Column(nullable = false)
    private String type;
    private String message;
    @Column(name = "created_by")
    protected String createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    protected Date createdDate;
    @Override
    public int hashCode() {
        return Objects.hash(key, locale, type);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    private String status;
}
