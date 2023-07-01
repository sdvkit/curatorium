package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.StudentCreationDto;
import com.sdv.kit.server.dto.StudentDto;
import com.sdv.kit.server.model.Student;
import org.mapstruct.Mapper;

@Mapper
public interface StudentMapper {
    Student toEntity(StudentDto studentDto);

    StudentDto toDto(Student student);

    Student toEntity(StudentCreationDto studentCreationDto);

    StudentCreationDto toCreationDto(Student student);
}