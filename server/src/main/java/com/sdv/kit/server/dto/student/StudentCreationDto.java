package com.sdv.kit.server.dto.student;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record StudentCreationDto(@NotNull Long groupId,
                                 @NotNull @Size(min = 4, max = 50) String name) implements Serializable {
}