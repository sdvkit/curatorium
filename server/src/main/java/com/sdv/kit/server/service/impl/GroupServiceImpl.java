package com.sdv.kit.server.service.impl;

import com.sdv.kit.server.dto.GroupDto;
import com.sdv.kit.server.mapper.GroupMapper;
import com.sdv.kit.server.repository.GroupRepository;
import com.sdv.kit.server.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private static final GroupMapper GROUP_MAPPER = Mappers.getMapper(GroupMapper.class);

    private final GroupRepository groupRepository;

    @Override
    public List<GroupDto> findAllByUser(String username) {
        return groupRepository.findAllByUser(username).stream()
                .map(GROUP_MAPPER::toDto)
                .toList();
    }
}
