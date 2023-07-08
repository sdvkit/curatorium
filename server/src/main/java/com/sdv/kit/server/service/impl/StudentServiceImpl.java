package com.sdv.kit.server.service.impl;

import com.sdv.kit.server.dto.student.StudentCreationDto;
import com.sdv.kit.server.dto.student.StudentRenameDto;
import com.sdv.kit.server.facade.AuthFacade;
import com.sdv.kit.server.mapper.StudentMapper;
import com.sdv.kit.server.model.Student;
import com.sdv.kit.server.repository.StudentRepository;
import com.sdv.kit.server.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private static final StudentMapper STUDENT_MAPPER = Mappers.getMapper(StudentMapper.class);

    private final StudentRepository studentRepository;

    private final AuthFacade authFacade;

    @Transactional
    @CachePut(value = "students")
    @Override
    public Optional<Student> save(StudentCreationDto studentCreationDto) {
        final Student student = STUDENT_MAPPER.toEntity(studentCreationDto);
        return Optional.of(studentRepository.save(student));
    }

    @Transactional
    @CacheEvict(value = "students")
    @Override
    public void delete(Long studentId) {
        final Student student = studentRepository.findByIdAndUser(studentId, authFacade.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        studentRepository.delete(student);
    }

    @Transactional
    @CachePut(value = "students")
    @Override
    public Optional<Student> rename(Long studentId, StudentRenameDto studentRenameDto) {
        final Student student = studentRepository.findByIdAndUser(studentId, authFacade.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        student.setName(studentRenameDto.name());
        return Optional.of(studentRepository.save(student));
    }

}
