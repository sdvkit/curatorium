package com.sdv.kit.server.dto;

import com.sdv.kit.server.dto.SubjectNestedDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

public record FirstLvlStatementCreationDto(@NotNull @Size(min = 4, max = 50) String name,
                                           @NotNull List<SubjectNestedDto> subjects,
                                           @NotNull Long groupId) implements Serializable {
}