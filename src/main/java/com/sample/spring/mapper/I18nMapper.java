package com.sample.spring.mapper;

import com.sample.spring.dto.I18nDTO;
import com.sample.spring.entity.I18nEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface I18nMapper {
    I18nMapper INSTANCE = Mappers.getMapper(I18nMapper.class);

    List<I18nEntity> from(List<I18nDTO> dto);
}
