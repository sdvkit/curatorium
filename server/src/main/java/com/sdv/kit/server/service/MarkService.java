package com.sdv.kit.server.service;

import com.sdv.kit.server.dto.MarkCreationDto;
import com.sdv.kit.server.dto.MarkDto;
import com.sdv.kit.server.dto.MarkEditDto;

import java.util.concurrent.CompletableFuture;

public interface MarkService {

    CompletableFuture<MarkDto> save(MarkCreationDto markCreationDto);

    void edit(Long markId, MarkEditDto markEditDto, String username);
}
