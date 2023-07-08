package com.sdv.kit.server.dto.group;

import com.sdv.kit.server.dto.student.StudentDto;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record GroupDto(@NotNull Long id,
                       @NotNull String name,
                       @NotNull LocalDate createdAt,
                       @NotNull Integer groupYear,
                       @NotNull Boolean isArchived,
                       @NotNull List<StudentDto> students) implements Serializable {
}