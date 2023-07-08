package com.sdv.kit.server.service;

import com.sdv.kit.server.dto.student.StudentCreationDto;
import com.sdv.kit.server.dto.student.StudentRenameDto;
import com.sdv.kit.server.model.Student;

import java.util.Optional;

public interface StudentService {

    Optional<Student> save(StudentCreationDto studentCreationDto);

    void delete(Long studentId);

    Optional<Student> rename(Long studentId, StudentRenameDto studentRenameDto);
}
