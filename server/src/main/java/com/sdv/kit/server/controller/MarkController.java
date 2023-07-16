package com.sdv.kit.server.controller;

import com.sdv.kit.server.dto.MarkCreationDto;
import com.sdv.kit.server.dto.MarkDto;
import com.sdv.kit.server.dto.MarkEditDto;
import com.sdv.kit.server.service.MarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/marks")
@RequiredArgsConstructor
public class MarkController {

    private final MarkService markService;

    @PostMapping
    @SneakyThrows
    public ResponseEntity<MarkDto> addMark(@RequestBody @Valid MarkCreationDto markCreationDto,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(markService
                        .save(markCreationDto)
                        .get());
    }

    @PatchMapping("/{markId}")
    @SneakyThrows
    public ResponseEntity<MarkDto> editMark(@PathVariable Long markId,
                                            @RequestBody @Valid MarkEditDto markEditDto,
                                            BindingResult bindingResult,
                                            Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(markService
                        .edit(markId, markEditDto, authentication.getName())
                        .get());
    }
}
