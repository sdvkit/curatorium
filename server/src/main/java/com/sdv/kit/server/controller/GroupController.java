package com.sdv.kit.server.controller;

import com.sdv.kit.server.dto.GroupDto;
import com.sdv.kit.server.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<List<GroupDto>> getUserGroups(Authentication authentication) {
        final List<GroupDto> groupDtoList = groupService.findAllByUser(authentication.getName());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupDtoList);
    }
}
