package com.sdv.kit.server.controller;

import com.sdv.kit.server.dto.SubjectCreationDto;
import com.sdv.kit.server.dto.SubjectDto;
import com.sdv.kit.server.dto.SubjectRenameDto;
import com.sdv.kit.server.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @SneakyThrows
    public ResponseEntity<List<SubjectDto>> getUserSubjects(Authentication authentication) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(subjectService
                        .findAllByUser(authentication.getName())
                        .get());
    }

    @PostMapping
    @SneakyThrows
    public ResponseEntity<SubjectDto> addSubject(@RequestBody @Valid SubjectCreationDto subjectCreationDto,
                                                 BindingResult bindingResult,
                                                 Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(subjectService
                        .save(subjectCreationDto, authentication.getName())
                        .get());
    }

    @PatchMapping("/{subjectId}")
    @SneakyThrows
    public ResponseEntity<SubjectDto> renameSubject(@PathVariable Long subjectId,
                                                    @RequestBody @Valid SubjectRenameDto subjectRenameDto,
                                                    BindingResult bindingResult,
                                                    Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        subjectService.rename(subjectId, subjectRenameDto, authentication.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<HttpStatus> deleteSubject(@PathVariable Long subjectId,
                                                    Authentication authentication) {
        subjectService.delete(subjectId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
