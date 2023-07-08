package com.sdv.kit.server.service.impl;

import com.sdv.kit.server.dto.group.GroupCreationDto;
import com.sdv.kit.server.dto.group.GroupDto;
import com.sdv.kit.server.dto.group.GroupRenameDto;
import com.sdv.kit.server.facade.AuthFacade;
import com.sdv.kit.server.mapper.GroupMapper;
import com.sdv.kit.server.model.Group;
import com.sdv.kit.server.model.User;
import com.sdv.kit.server.repository.GroupRepository;
import com.sdv.kit.server.repository.UserRepository;
import com.sdv.kit.server.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private static final GroupMapper GROUP_MAPPER = Mappers.getMapper(GroupMapper.class);

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    private final AuthFacade authFacade;

    @Override
    public List<GroupDto> findAllByUser() {
        return groupRepository.findAllByUser(authFacade.getName()).stream()
                .map(GROUP_MAPPER::toDto)
                .toList();
    }

    @Transactional
    @CachePut(value = "groups")
    @Override
    public Optional<Group> save(GroupCreationDto groupCreationDto) {
        groupRepository.findExistsGroup(groupCreationDto.name(), groupCreationDto.groupYear(), authFacade.getName())
                .ifPresent(group -> {
                    final String message = String.format("Group with name %s and year %d already exists.",
                            groupCreationDto.name(), groupCreationDto.groupYear());
                    throw new ResponseStatusException(HttpStatus.CONFLICT, message);
                });

        final User user = userRepository.findByUsername(authFacade.getName())
                .orElseThrow(() -> new UsernameNotFoundException("There's no user with this username"));

        final Group group = GROUP_MAPPER.toEntity(groupCreationDto);
        group.setUser(user);
        group.setIsArchived(false);
        group.setCreatedAt(LocalDate.now());
        return Optional.of(groupRepository.save(group));
    }

    @Transactional
    @CachePut(value = "groups")
    @Override
    public Optional<Group> rename(Long groupId, GroupRenameDto groupRenameDto) {
        final Group group = groupRepository.findByIdAndUser(groupId, authFacade.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        group.setName(groupRenameDto.name());
        return Optional.of(groupRepository.save(group));
    }

    @Transactional
    @CacheEvict(value = "groups")
    @Override
    public void delete(Long groupId) {
        final Group group = groupRepository.findByIdAndUser(groupId, authFacade.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        groupRepository.delete(group);
    }

    @Transactional
    @CachePut(value = "groups")
    @Override
    public Optional<Group> archive(Long groupId) {
        final Group group = groupRepository.findByIdAndUser(groupId, authFacade.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        final boolean isArchived = group.getIsArchived();
        group.setIsArchived(!isArchived);
        return Optional.of(groupRepository.save(group));
    }
}
