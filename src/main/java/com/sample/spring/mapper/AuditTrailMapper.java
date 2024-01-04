package com.sample.spring.mapper;

import com.sample.spring.audit.dto.AuditTrailDto;
import com.sample.spring.entity.AuditTrailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuditTrailMapper {
    AuditTrailMapper INSTANCE = Mappers.getMapper(AuditTrailMapper.class);

    AuditTrailEntity dtoToEntity(AuditTrailDto dto);
}
