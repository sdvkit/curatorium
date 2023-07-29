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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MarkServiceImpl implements MarkService {

    private static final MarkMapper MARK_MAPPER = Mappers.getMapper(MarkMapper.class);

    private final MarkRepository markRepository;

    @Async
    @Transactional
    @Override
    public CompletableFuture<MarkDto> save(MarkCreationDto markCreationDto) {
        final Mark mark = MARK_MAPPER.toEntity(markCreationDto);

        mark.setMarkType(MarkType.fromIndex(markCreationDto.markType()));

        final MarkDto savedMarkDto = MARK_MAPPER.toDto(markRepository.save(mark));
        return CompletableFuture.completedFuture(savedMarkDto);
    }

    @Async
    @Transactional
    @Override
    public void edit(Long markId, MarkEditDto markEditDto, String username) {
        markRepository.editMark(markId, markEditDto.value(), username);
    }
}
