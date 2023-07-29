package com.sdv.kit.server.service;

import com.sdv.kit.server.dto.FirstLvlStatementCreationDto;
import com.sdv.kit.server.dto.FirstLvlStatementDto;
import com.sdv.kit.server.dto.FirstLvlStatementRenameDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FirstLvlStatementService {

    CompletableFuture<FirstLvlStatementDto> save(FirstLvlStatementCreationDto flsCreationDto, String username);

    void rename(Long firstLvlStatementId, FirstLvlStatementRenameDto flsRenameDto, String username);

    CompletableFuture<List<FirstLvlStatementDto>> findAllByUser(String username);

    void delete(Long firstLvlStatementId, String username);
}
