package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.sls.SecondLvlStatementCreationDto;
import com.sdv.kit.server.dto.sls.SecondLvlStatementDto;
import com.sdv.kit.server.model.SecondLvlStatement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {FirstLvlStatementMapper.class})
public interface SecondLvlStatementMapper {

    SecondLvlStatement toEntity(SecondLvlStatementDto secondLvlStatementDto);

    SecondLvlStatementDto toDto(SecondLvlStatement secondLvlStatement);

    @Mapping(source = "firstLvlStatementId", target = "firstLvlStatement.id")
    SecondLvlStatement toEntity(SecondLvlStatementCreationDto secondLvlStatementCreationDto);

    @Mapping(source = "firstLvlStatement.id", target = "firstLvlStatementId")
    SecondLvlStatementCreationDto toCreationDto(SecondLvlStatement secondLvlStatement);
}