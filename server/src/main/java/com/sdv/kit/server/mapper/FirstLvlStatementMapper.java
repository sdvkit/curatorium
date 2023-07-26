package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.FirstLvlStatementCreationDto;
import com.sdv.kit.server.dto.FirstLvlStatementDto;
import com.sdv.kit.server.dto.FirstLvlStatementRenameDto;
import com.sdv.kit.server.model.FirstLvlStatement;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(uses = {SecondLvlStatementMapper.class, GroupMapper.class})
public interface FirstLvlStatementMapper {

    @Mapping(source = "groupId", target = "group.id")
    @Mapping(source = "secondLvlStatements", target = "secondLvlStatements")
    FirstLvlStatement toEntity(FirstLvlStatementDto firstLvlStatementDto);

    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "secondLvlStatements", target = "secondLvlStatements")
    FirstLvlStatementDto toDto(FirstLvlStatement firstLvlStatement);

    @Mapping(source = "groupId", target = "group.id")
    FirstLvlStatement toEntity(FirstLvlStatementCreationDto firstLvlStatementCreationDto);

    @Mapping(source = "group.id", target = "groupId")
    FirstLvlStatementCreationDto toCreationDto(FirstLvlStatement firstLvlStatement);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    FirstLvlStatement partialUpdate(FirstLvlStatementDto firstLvlStatementDto, @MappingTarget FirstLvlStatement firstLvlStatement);

    FirstLvlStatement toEntity(FirstLvlStatementRenameDto firstLvlStatementRenameDto);

    FirstLvlStatementRenameDto toRenameDto(FirstLvlStatement firstLvlStatement);
}