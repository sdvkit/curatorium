package com.sdv.kit.server.controller;

import com.sdv.kit.server.dto.GroupCreationDto;
import com.sdv.kit.server.dto.GroupDto;
import com.sdv.kit.server.dto.GroupRenameDto;
import com.sdv.kit.server.service.GroupService;
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
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    @SneakyThrows
    public ResponseEntity<List<GroupDto>> getUserGroup(Authentication authentication) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService
                        .findAllByUser(authentication.getName())
                        .get());
    }

    @PostMapping
    @SneakyThrows
    public ResponseEntity<GroupDto> addGroup(@RequestBody @Valid GroupCreationDto groupCreationDto,
                                               BindingResult bindingResult,
                                               Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService
                        .save(groupCreationDto, authentication.getName())
                        .get());
    }

    @PatchMapping("/{groupId}")
    @SneakyThrows
    public ResponseEntity<GroupDto> renameGroup(@PathVariable Long groupId,
                                                  @RequestBody @Valid GroupRenameDto groupRenameDto,
                                                  BindingResult bindingResult,
                                                  Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        groupService.rename(groupId, groupRenameDto, authentication.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<HttpStatus> deleteGroup(@PathVariable Long groupId,
                                                  Authentication authentication) {
        groupService.delete(groupId, authentication.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/archive/{groupId}")
    @SneakyThrows
    public ResponseEntity<GroupDto> archiveGroup(@PathVariable Long groupId,
                                                   Authentication authentication) {

        groupService.archive(groupId, authentication.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }
}
