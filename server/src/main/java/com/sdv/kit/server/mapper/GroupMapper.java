package com.sdv.kit.server.mapper;

import com.sdv.kit.server.dto.GroupDto;
import com.sdv.kit.server.model.Group;
import org.mapstruct.Mapper;

@Mapper
public interface GroupMapper {
    Group toEntity(GroupDto groupDto);

    GroupDto toDto(Group group);
}