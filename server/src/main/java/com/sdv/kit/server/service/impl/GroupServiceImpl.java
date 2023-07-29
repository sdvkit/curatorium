package com.sdv.kit.server.service.impl;

import com.sdv.kit.server.dto.GroupCreationDto;
import com.sdv.kit.server.dto.GroupDto;
import com.sdv.kit.server.dto.GroupRenameDto;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private static final GroupMapper GROUP_MAPPER = Mappers.getMapper(GroupMapper.class);

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    @Async
    @Override
    public CompletableFuture<List<GroupDto>> findAllByUser(String username) {
        return CompletableFuture.completedFuture(groupRepository
                .findAllByUser(username)
                .stream()
                .map(GROUP_MAPPER::toDto)
                .toList());
    }

    @Async
    @Transactional
    @Cacheable(value = "groups")
    @Override
    public CompletableFuture<GroupDto> save(GroupCreationDto groupCreationDto, String username) {
        groupRepository.findExistsGroup(groupCreationDto.name(), groupCreationDto.groupYear(), username)
                .ifPresent(group -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT);
                });

        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("There's no user with this username"));

        final Group group = GROUP_MAPPER.toEntity(groupCreationDto);
        group.setUser(user);
        group.setIsArchived(false);
        group.setCreatedAt(LocalDate.now());

        final GroupDto savedGroupDto = GROUP_MAPPER.toDto(groupRepository.save(group));
        return CompletableFuture.completedFuture(savedGroupDto);
    }

    @Async
    @Transactional
    @CachePut(value = "groups")
    @Override
    public void rename(Long groupId, GroupRenameDto groupRenameDto, String username) {
        groupRepository.renameGroup(groupId, groupRenameDto.name(), username);
    }

    @Async
    @Transactional
    @CacheEvict(value = "groups")
    @Override
    public void delete(Long groupId, String username) {
        final Group group = groupRepository.findByIdAndUser(groupId, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        groupRepository.delete(group);
    }

    @Async
    @Transactional
    @CachePut(value = "groups")
    @Override
    public void archive(Long groupId, String username) {
        groupRepository.archiveGroup(groupId, username);
    }
}
