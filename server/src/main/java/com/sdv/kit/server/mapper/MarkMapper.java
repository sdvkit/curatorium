package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.MarkCreationDto;
import com.sdv.kit.server.dto.MarkDto;
import com.sdv.kit.server.dto.MarkEditDto;
import com.sdv.kit.server.model.Mark;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {SecondLvlStatementMapper.class, StudentMapper.class})
public interface MarkMapper {

    @Mapping(source = "secondLvlStatementId", target = "secondLvlStatement.id")
    Mark toEntity(MarkDto markDto);

    @Mapping(source = "secondLvlStatement.id", target = "secondLvlStatementId")
    MarkDto toDto(Mark mark);

    @Mapping(source = "secondLvlStatementId", target = "secondLvlStatement.id")
    @Mapping(source = "studentId", target = "student.id")
    @Mapping(target = "markType", ignore = true)
    Mark toEntity(MarkCreationDto markCreationDto);

    @Mapping(source = "secondLvlStatement.id", target = "secondLvlStatementId")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(target = "markType", ignore = true)
    MarkCreationDto toCreationDto(Mark mark);

    Mark toEntity(MarkEditDto markEditDto);

    MarkEditDto toEditDto(Mark mark);
}