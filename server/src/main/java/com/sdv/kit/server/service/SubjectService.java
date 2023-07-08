package com.sdv.kit.server.service;

import com.sdv.kit.server.dto.subject.SubjectCreationDto;
import com.sdv.kit.server.dto.subject.SubjectDto;
import com.sdv.kit.server.model.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectService {

    Optional<Subject> save(SubjectCreationDto subjectCreationDto);

    List<SubjectDto> findAllByUser();
}
