package com.sdv.kit.server.service.impl;

import com.sdv.kit.server.dto.SubjectCreationDto;
import com.sdv.kit.server.dto.SubjectDto;
import com.sdv.kit.server.dto.SubjectRenameDto;
import com.sdv.kit.server.mapper.SubjectMapper;
import com.sdv.kit.server.model.Subject;
import com.sdv.kit.server.model.User;
import com.sdv.kit.server.repository.SubjectRepository;
import com.sdv.kit.server.repository.UserRepository;
import com.sdv.kit.server.service.SubjectService;
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

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private static final SubjectMapper SUBJECT_MAPPER = Mappers.getMapper(SubjectMapper.class);

    private final SubjectRepository subjectRepository;

    private final UserRepository userRepository;

    @Async
    @Transactional
    @Cacheable(value = "subjects")
    @Override
    public CompletableFuture<SubjectDto> save(SubjectCreationDto subjectCreationDto, String username) {
        subjectRepository.findExistsSubject(subjectCreationDto.name(), username)
                .ifPresent(subject -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT);
                });

        final User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("There's no user with this username"));

        final Subject subject = SUBJECT_MAPPER.toEntity(subjectCreationDto);
        subject.setUser(user);

        final SubjectDto savedSubjectDto = SUBJECT_MAPPER.toDto(subjectRepository.save(subject));
        return CompletableFuture.completedFuture(savedSubjectDto);
    }

    @Async
    @Override
    public CompletableFuture<List<SubjectDto>> findAllByUser(String username) {
        return CompletableFuture.completedFuture(subjectRepository
                .findAllByUser(username)
                .stream()
                .map(SUBJECT_MAPPER::toDto)
                .toList());
    }

    @Async
    @CachePut(value = "subjects")
    @Transactional
    @Override
    public void rename(Long subjectId, SubjectRenameDto subjectRenameDto, String username) {
        subjectRepository.renameSubject(subjectId, subjectRenameDto.name(), username);
    }

    @Async
    @CacheEvict(value = "subjects")
    @Transactional
    @Override
    public void delete(Long subjectId, String name) {
        final Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        subjectRepository.delete(subject);
    }
}
