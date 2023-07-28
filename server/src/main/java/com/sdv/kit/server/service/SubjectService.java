package com.sdv.kit.server.service;

import com.sdv.kit.server.dto.SubjectCreationDto;
import com.sdv.kit.server.dto.SubjectDto;
import com.sdv.kit.server.dto.SubjectRenameDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface SubjectService {

    CompletableFuture<SubjectDto> save(SubjectCreationDto subjectCreationDto, String username);

    CompletableFuture<List<SubjectDto>> findAllByUser(String username);

    void rename(Long subjectId, SubjectRenameDto subjectRenameDto, String username);

    void delete(Long subjectId, String username);
}
