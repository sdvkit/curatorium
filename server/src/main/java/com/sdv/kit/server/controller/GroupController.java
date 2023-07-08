package com.sdv.kit.server.controller;

import com.sdv.kit.server.dto.group.GroupCreationDto;
import com.sdv.kit.server.dto.group.GroupDto;
import com.sdv.kit.server.dto.group.GroupRenameDto;
import com.sdv.kit.server.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<GroupDto>> getUserGroup() {
        final List<GroupDto> groupDtoList = groupService.findAllByUser();
        return new ResponseEntity<>(groupDtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> addGroup(@RequestBody @Valid GroupCreationDto groupCreationDto,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return groupService.save(groupCreationDto).isPresent()
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<HttpStatus> renameGroup(@PathVariable Long groupId,
                                                  @RequestBody @Valid GroupRenameDto groupRenameDto,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        return groupService.rename(groupId, groupRenameDto).isPresent()
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<HttpStatus> deleteGroup(@PathVariable Long groupId) {
        groupService.delete(groupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/archive/{groupId}")
    public ResponseEntity<HttpStatus> archiveGroup(@PathVariable Long groupId) {
        return groupService.archive(groupId).isPresent()
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
