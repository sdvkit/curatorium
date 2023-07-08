package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.student.StudentCreationDto;
import com.sdv.kit.server.dto.student.StudentDto;
import com.sdv.kit.server.dto.student.StudentRenameDto;
import com.sdv.kit.server.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface StudentMapper {

    Student toEntity(StudentDto studentDto);

    StudentDto toDto(Student student);

    @Mapping(source = "groupId", target = "group.id")
    Student toEntity(StudentCreationDto studentCreationDto);

    @Mapping(source = "group.id", target = "groupId")
    StudentCreationDto toCreationDto(Student student);

    Student toEntity(StudentRenameDto studentRenameDto);

    StudentRenameDto toRenameDto(Student student);
}