package com.sdv.kit.server.controller;

import com.sdv.kit.server.dto.subject.SubjectCreationDto;
import com.sdv.kit.server.dto.subject.SubjectDto;
import com.sdv.kit.server.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<SubjectDto>> getUserSubjects() {
        final List<SubjectDto> subjectDtoList = subjectService.findAllByUser();
        return new ResponseEntity<>(subjectDtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addSubject(@RequestBody @Valid SubjectCreationDto subjectCreationDto,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return subjectService.save(subjectCreationDto).isPresent()
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
