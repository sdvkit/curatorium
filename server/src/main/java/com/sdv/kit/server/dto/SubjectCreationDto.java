package com.sdv.kit.server.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record SubjectCreationDto(@NotNull @Size(min = 2, max = 50) String name,
                                 @NotNull Long userId) implements Serializable {
}