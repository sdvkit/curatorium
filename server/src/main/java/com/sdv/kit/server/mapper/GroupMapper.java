package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.group.GroupCreationDto;
import com.sdv.kit.server.dto.group.GroupDto;
import com.sdv.kit.server.dto.group.GroupRenameDto;
import com.sdv.kit.server.model.Group;
import org.mapstruct.Mapper;

@Mapper(uses = {UserMapper.class})
public interface GroupMapper {

    Group toEntity(GroupDto groupDto);

    GroupDto toDto(Group group);

    Group toEntity(GroupCreationDto groupCreationDto);

    GroupCreationDto toCreationDto(Group group);

    Group toEntity(GroupRenameDto groupRenameDto);

    GroupRenameDto toRenameDto(Group group);
}