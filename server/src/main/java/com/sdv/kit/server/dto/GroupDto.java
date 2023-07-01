package com.sdv.kit.server.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

public record GroupDto(@NotNull Long id,
                       @NotNull String name,
                       @NotNull LocalDate createdAt,
                       @NotNull Boolean isArchived) implements Serializable {
}