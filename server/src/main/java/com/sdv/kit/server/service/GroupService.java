package com.sdv.kit.server.service;

import com.sdv.kit.server.dto.group.GroupCreationDto;
import com.sdv.kit.server.dto.group.GroupDto;
import com.sdv.kit.server.dto.group.GroupRenameDto;
import com.sdv.kit.server.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    List<GroupDto> findAllByUser();

    Optional<Group> save(GroupCreationDto groupCreationDto);

    Optional<Group> rename(Long groupId, GroupRenameDto groupRenameDto);

    void delete(Long groupId);

    Optional<Group> archive(Long groupId);
}
