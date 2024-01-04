package com.sample.spring.common.model;

import com.sample.spring.common.sequence.annotation.InjectSequenceValue;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Objects;

@Getter
@Setter
@FieldNameConstants
public class IdentityEntity {
    @Id
    @Indexed
    @InjectSequenceValue
    protected String _id;
    @Transient
    protected String id;

    public String getId() {
        return (ObjectUtils.isNotEmpty(id)) ? id : _id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id);
    }

    @Override
    public boolean equals(Object that) {
        return that instanceof IdentityEntity && this._id != null && this._id.equals(((IdentityEntity) that).get_id());
    }
}
