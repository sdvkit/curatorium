package com.sdv.kit.server.controller;

import com.sdv.kit.server.dto.StudentCreationDto;
import com.sdv.kit.server.dto.StudentDto;
import com.sdv.kit.server.dto.StudentRenameDto;
import com.sdv.kit.server.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @SneakyThrows
    public ResponseEntity<StudentDto> addStudent(@RequestBody @Valid StudentCreationDto studentCreationDto,
                                                 BindingResult bindingResult,
                                                 Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentService
                        .save(studentCreationDto, authentication.getName())
                        .get());
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable Long studentId,
                                                    Authentication authentication) {
        studentService.delete(studentId, authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{studentId}")
    @SneakyThrows
    public ResponseEntity<StudentDto> renameStudent(@PathVariable Long studentId,
                                                    @RequestBody @Valid StudentRenameDto studentRenameDto,
                                                    BindingResult bindingResult,
                                                    Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(studentService
                        .rename(studentId, studentRenameDto, authentication.getName())
                        .get());
    }
}
