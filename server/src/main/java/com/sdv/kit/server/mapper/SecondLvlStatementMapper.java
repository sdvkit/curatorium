package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.SecondLvlStatementDto;
import com.sdv.kit.server.model.SecondLvlStatement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SecondLvlStatementMapper {

    @Mapping(source = "subjectId", target = "subject.id")
    SecondLvlStatement toEntity(SecondLvlStatementDto secondLvlStatementDto);

    @Mapping(source = "subject.id", target = "subjectId")
    SecondLvlStatementDto toDto(SecondLvlStatement secondLvlStatement);
}