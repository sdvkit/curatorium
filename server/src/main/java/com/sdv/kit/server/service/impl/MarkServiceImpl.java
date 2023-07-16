package com.sdv.kit.server.service.impl;

import com.sdv.kit.server.dto.MarkCreationDto;
import com.sdv.kit.server.dto.MarkDto;
import com.sdv.kit.server.dto.MarkEditDto;
import com.sdv.kit.server.mapper.MarkMapper;
import com.sdv.kit.server.model.Mark;
import com.sdv.kit.server.model.MarkType;
import com.sdv.kit.server.repository.MarkRepository;
import com.sdv.kit.server.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MarkServiceImpl implements MarkService {

    private static final MarkMapper MARK_MAPPER = Mappers.getMapper(MarkMapper.class);

    private final MarkRepository markRepository;

    @Async
    @Cacheable(value = "marks")
    @Transactional
    @Override
    public CompletableFuture<MarkDto> save(MarkCreationDto markCreationDto) {
        final Mark mark = MARK_MAPPER.toEntity(markCreationDto);
        mark.setMarkType(MarkType.fromIndex(markCreationDto.markType()));

        final MarkDto savedMarkDto = MARK_MAPPER.toDto(markRepository.save(mark));
        return CompletableFuture.completedFuture(savedMarkDto);
    }

    @Async
    @CachePut(value = "marks")
    @Transactional
    @Override
    public CompletableFuture<MarkDto> edit(Long markId, MarkEditDto markEditDto, String username) {
        final Mark mark = markRepository.findByIdAndUser(markId, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        mark.setValue(markEditDto.value());

        final MarkDto editedMarkDto = MARK_MAPPER.toDto(markRepository.save(mark));
        return CompletableFuture.completedFuture(editedMarkDto);
    }
}
