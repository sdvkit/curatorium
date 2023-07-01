package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.MarkCreationDto;
import com.sdv.kit.server.dto.MarkDto;
import com.sdv.kit.server.model.Mark;
import org.mapstruct.Mapper;

@Mapper
public interface MarkMapper {
    Mark toEntity(MarkDto markDto);

    MarkDto toDto(Mark mark);

    Mark toEntity(MarkCreationDto markCreationDto);

    MarkCreationDto toCreationDto(Mark mark);
}