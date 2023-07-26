package com.sdv.kit.server.service.impl;

import com.sdv.kit.server.dto.FirstLvlStatementCreationDto;
import com.sdv.kit.server.dto.FirstLvlStatementDto;
import com.sdv.kit.server.dto.FirstLvlStatementRenameDto;
import com.sdv.kit.server.mapper.FirstLvlStatementMapper;
import com.sdv.kit.server.model.FirstLvlStatement;
import com.sdv.kit.server.model.SecondLvlStatement;
import com.sdv.kit.server.model.Subject;
import com.sdv.kit.server.repository.FirstLvlStatementRepository;
import com.sdv.kit.server.repository.SecondLvlStatementRepository;
import com.sdv.kit.server.service.FirstLvlStatementService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FirstLvlStatementServiceImpl implements FirstLvlStatementService {

    private static final FirstLvlStatementMapper FLS_MAPPER = Mappers.getMapper(FirstLvlStatementMapper.class);

    private final FirstLvlStatementRepository firstLvlStatementRepository;

    private final SecondLvlStatementRepository secondLvlStatementRepository;

    @Async
    @Cacheable(value = "fls")
    @Transactional
    @Override
    public CompletableFuture<FirstLvlStatementDto> save(FirstLvlStatementCreationDto flsCreationDto, String username) {
        firstLvlStatementRepository.findByNameAndUser(flsCreationDto.name(), username)
                .ifPresent(firstLvlStatement -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT);
                });

        final FirstLvlStatement firstLvlStatement = FLS_MAPPER.toEntity(flsCreationDto);
        firstLvlStatementRepository.save(firstLvlStatement);

        final List<SecondLvlStatement> secondLvlStatementsList = new ArrayList<>();
        flsCreationDto.subjects().forEach(subjectNestedDto -> {
            final SecondLvlStatement secondLvlStatement = SecondLvlStatement.builder()
                    .firstLvlStatement(firstLvlStatement)
                    .subject(Subject.builder()
                            .id(subjectNestedDto.id())
                            .build())
                    .build();

            secondLvlStatementsList.add(secondLvlStatement);
        });

        secondLvlStatementRepository.saveAll(secondLvlStatementsList);

        firstLvlStatement.setSecondLvlStatements(secondLvlStatementsList);

        return CompletableFuture.completedFuture(FLS_MAPPER.toDto(firstLvlStatement));
    }

    @Async
    @CachePut(value = "fls")
    @Transactional
    @Override
    public CompletableFuture<FirstLvlStatementDto> rename(Long firstLvlStatementId, FirstLvlStatementRenameDto flsRenameDto, String username) {
        final FirstLvlStatement firstLvlStatement = firstLvlStatementRepository.findByIdAndUser(firstLvlStatementId, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        firstLvlStatement.setName(flsRenameDto.name());

        final FirstLvlStatementDto renamedFlsDto = FLS_MAPPER.toDto(firstLvlStatementRepository.save(firstLvlStatement));
        return CompletableFuture.completedFuture(renamedFlsDto);
    }

    @Async
    @Override
    public CompletableFuture<List<FirstLvlStatementDto>> findAllByUser(String username) {
        return CompletableFuture.completedFuture(firstLvlStatementRepository
                .findAllByUser(username)
                .stream()
                .map(FLS_MAPPER::toDto)
                .toList());
    }

    @Async
    @CacheEvict(value = "fls")
    @Transactional
    @Override
    public void delete(Long firstLvlStatementId, String username) {
        final FirstLvlStatement firstLvlStatement = firstLvlStatementRepository.findByIdAndUser(firstLvlStatementId, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        firstLvlStatementRepository.delete(firstLvlStatement);
    }
}
