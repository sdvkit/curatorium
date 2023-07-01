package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.SubjectCreationDto;
import com.sdv.kit.server.dto.SubjectDto;
import com.sdv.kit.server.model.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SubjectMapper {
    Subject toEntity(SubjectDto subjectDto);

    SubjectDto toDto(Subject subject);

    @Mapping(source = "userId", target = "user.id")
    Subject toEntity(SubjectCreationDto subjectCreationDto);

    @Mapping(source = "user.id", target = "userId")
    SubjectCreationDto toCreationDto(Subject subject);
}