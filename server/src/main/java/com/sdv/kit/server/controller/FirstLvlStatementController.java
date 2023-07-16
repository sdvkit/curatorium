package com.sdv.kit.server.controller;

import com.sdv.kit.server.dto.FirstLvlStatementCreationDto;
import com.sdv.kit.server.dto.FirstLvlStatementDto;
import com.sdv.kit.server.dto.FirstLvlStatementRenameDto;
import com.sdv.kit.server.service.FirstLvlStatementService;
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
@RequestMapping("/api/v1/fls")
@RequiredArgsConstructor
public class FirstLvlStatementController {

    private final FirstLvlStatementService firstLvlStatementService;

    @PostMapping
    @SneakyThrows
    public ResponseEntity<FirstLvlStatementDto> addFirstLvlStatement(@RequestBody @Valid FirstLvlStatementCreationDto flsCreationDto,
                                                                     BindingResult bindingResult,
                                                                     Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(firstLvlStatementService
                        .save(flsCreationDto, authentication.getName())
                        .get());
    }

    @PatchMapping("/{firstLvlStatementId}")
    @SneakyThrows
    public ResponseEntity<FirstLvlStatementDto> renameFirstLvlStatement(@PathVariable Long firstLvlStatementId,
                                                                        @RequestBody @Valid FirstLvlStatementRenameDto flsRenameDto,
                                                                        BindingResult bindingResult,
                                                                        Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(firstLvlStatementService
                        .rename(firstLvlStatementId, flsRenameDto, authentication.getName())
                        .get());
    }

    @GetMapping
    @SneakyThrows
    public ResponseEntity<List<FirstLvlStatementDto>> getUsersFirstLvlStatements(Authentication authentication) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(firstLvlStatementService
                        .findAllByUser(authentication.getName())
                        .get());
    }

    @DeleteMapping("/{firstLvlStatementId}")
    public ResponseEntity<HttpStatus> deleteFirstLvlStatement(@PathVariable Long firstLvlStatementId,
                                                              Authentication authentication) {
        firstLvlStatementService.delete(firstLvlStatementId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
