package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.SubjectCreationDto;
import com.sdv.kit.server.dto.SubjectDto;
import com.sdv.kit.server.dto.SubjectNestedDto;
import com.sdv.kit.server.dto.SubjectRenameDto;
import com.sdv.kit.server.model.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SubjectMapper {

    @Mapping(source = "userId", target = "user.id")
    Subject toEntity(SubjectDto subjectDto);

    @Mapping(source = "user.id", target = "userId")
    SubjectDto toDto(Subject subject);

    Subject toEntity(SubjectCreationDto subjectCreationDto);

    SubjectCreationDto toCreationDto(Subject subject);

    Subject toEntity(SubjectRenameDto subjectRenameDto);

    SubjectRenameDto toRenameDto(Subject subject);

    Subject toEntity(SubjectNestedDto subjectNestedDto);

    SubjectNestedDto toNestedDto(Subject subject);
}