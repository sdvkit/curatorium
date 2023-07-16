package com.sdv.kit.server.service;

import com.sdv.kit.server.dto.StudentCreationDto;
import com.sdv.kit.server.dto.StudentDto;
import com.sdv.kit.server.dto.StudentRenameDto;

import java.util.concurrent.CompletableFuture;

public interface StudentService {

    CompletableFuture<StudentDto> save(StudentCreationDto studentCreationDto, String username);

    void delete(Long studentId, String username);

    CompletableFuture<StudentDto> rename(Long studentId, StudentRenameDto studentRenameDto, String username);
}
