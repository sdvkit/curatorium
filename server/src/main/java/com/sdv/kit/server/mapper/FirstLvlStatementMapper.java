package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.FirstLvlStatementCreationDto;
import com.sdv.kit.server.dto.FirstLvlStatementDto;
import com.sdv.kit.server.model.FirstLvlStatement;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface FirstLvlStatementMapper {
    FirstLvlStatement toEntity(FirstLvlStatementDto firstLvlStatementDto);

    FirstLvlStatementDto toDto(FirstLvlStatement firstLvlStatement);

    FirstLvlStatement toEntity(FirstLvlStatementCreationDto firstLvlStatementCreationDto);

    FirstLvlStatementCreationDto toCreationDto(FirstLvlStatement firstLvlStatement);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    FirstLvlStatement partialUpdate(FirstLvlStatementDto firstLvlStatementDto, @MappingTarget FirstLvlStatement firstLvlStatement);
}