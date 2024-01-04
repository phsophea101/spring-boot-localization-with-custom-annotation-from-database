package com.sample.spring.entity;

import com.sample.spring.common.model.AuditEntity;
import com.sample.spring.common.model.RecyclableEntity;
import com.sample.spring.enums.StatusType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = MongoSequenceEntity.COLLECTION_NAME)
@FieldNameConstants
public class MongoSequenceEntity extends AuditEntity implements RecyclableEntity<String> {
    public static final String COLLECTION_NAME = "eco_sequences";
    private String seq;
    private Long current;
    private Long max;
    private Long increment;
    private Boolean recycle;
    private String status = String.valueOf(StatusType.ACTIVE);
}
