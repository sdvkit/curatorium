package com.sdv.kit.server.controller;

import com.sdv.kit.server.dto.student.StudentCreationDto;
import com.sdv.kit.server.dto.student.StudentRenameDto;
import com.sdv.kit.server.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class StudentsController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<HttpStatus> addStudent(@RequestBody @Valid StudentCreationDto studentCreationDto,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return studentService.save(studentCreationDto).isPresent()
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable Long studentId) {
        studentService.delete(studentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{studentId}")
    public ResponseEntity<HttpStatus> renameStudent(@PathVariable Long studentId,
                                                    @RequestBody @Valid StudentRenameDto studentRenameDto,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return studentService.rename(studentId, studentRenameDto).isPresent()
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
