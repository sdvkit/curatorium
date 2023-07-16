package com.sdv.kit.server.service;

import com.sdv.kit.server.dto.GroupCreationDto;
import com.sdv.kit.server.dto.GroupDto;
import com.sdv.kit.server.dto.GroupRenameDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface GroupService {

    CompletableFuture<List<GroupDto>> findAllByUser(String username);

    CompletableFuture<GroupDto> save(GroupCreationDto groupCreationDto, String username);

    CompletableFuture<GroupDto> rename(Long groupId, GroupRenameDto groupRenameDto, String username);

    void delete(Long groupId, String username);

    CompletableFuture<GroupDto> archive(Long groupId, String username);
}
