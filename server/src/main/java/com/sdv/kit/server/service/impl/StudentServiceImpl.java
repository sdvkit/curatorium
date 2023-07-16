package com.sdv.kit.server.service.impl;

import com.sdv.kit.server.dto.StudentCreationDto;
import com.sdv.kit.server.dto.StudentDto;
import com.sdv.kit.server.dto.StudentRenameDto;
import com.sdv.kit.server.mapper.StudentMapper;
import com.sdv.kit.server.model.Student;
import com.sdv.kit.server.repository.StudentRepository;
import com.sdv.kit.server.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private static final StudentMapper STUDENT_MAPPER = Mappers.getMapper(StudentMapper.class);

    private final StudentRepository studentRepository;

    @Async
    @Transactional
    @Cacheable(value = "students")
    @Override
    public CompletableFuture<StudentDto> save(StudentCreationDto studentCreationDto, String username) {
        final Student student = STUDENT_MAPPER.toEntity(studentCreationDto);
        final StudentDto savedStudentDto = STUDENT_MAPPER.toDto(studentRepository.save(student));
        return CompletableFuture.completedFuture(savedStudentDto);
    }

    @Async
    @Transactional
    @CacheEvict(value = "students")
    @Override
    public void delete(Long studentId, String username) {
        final Student student = studentRepository.findByIdAndUser(studentId, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        studentRepository.delete(student);
    }

    @Async
    @Transactional
    @CachePut(value = "students")
    @Override
    public CompletableFuture<StudentDto> rename(Long studentId, StudentRenameDto studentRenameDto, String username) {
        final Student student = studentRepository.findByIdAndUser(studentId, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        student.setName(studentRenameDto.name());

        final StudentDto renamedStudentDto = STUDENT_MAPPER.toDto(studentRepository.save(student));
        return CompletableFuture.completedFuture(renamedStudentDto);
    }

}
