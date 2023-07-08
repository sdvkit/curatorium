package com.sdv.kit.server.service.impl;

import com.sdv.kit.server.dto.subject.SubjectCreationDto;
import com.sdv.kit.server.dto.subject.SubjectDto;
import com.sdv.kit.server.facade.AuthFacade;
import com.sdv.kit.server.mapper.SubjectMapper;
import com.sdv.kit.server.model.Subject;
import com.sdv.kit.server.model.User;
import com.sdv.kit.server.repository.SubjectRepository;
import com.sdv.kit.server.repository.UserRepository;
import com.sdv.kit.server.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private static final SubjectMapper SUBJECT_MAPPER = Mappers.getMapper(SubjectMapper.class);

    private final SubjectRepository subjectRepository;

    private final UserRepository userRepository;

    private final AuthFacade authFacade;

    @Transactional
    @Cacheable(value = "subjects")
    @Override
    public Optional<Subject> save(SubjectCreationDto subjectCreationDto) {
        subjectRepository.findExistsSubject(subjectCreationDto.name(), authFacade.getName())
                .ifPresent(subject -> {
                    final String message = String.format("Subject with name %s already exists.", subjectCreationDto.name());
                    throw new ResponseStatusException(HttpStatus.CONFLICT, message);
                });

        final User user = userRepository.findByUsername(authFacade.getName())
                .orElseThrow(() -> new UsernameNotFoundException("There's no user with this username"));

        final Subject subject = SUBJECT_MAPPER.toEntity(subjectCreationDto);
        subject.setUser(user);
        return Optional.of(subjectRepository.save(subject));
    }

    @Override
    public List<SubjectDto> findAllByUser() {
        return subjectRepository.findAllByUser(authFacade.getName()).stream()
                .map(SUBJECT_MAPPER::toDto)
                .toList();
    }
}
