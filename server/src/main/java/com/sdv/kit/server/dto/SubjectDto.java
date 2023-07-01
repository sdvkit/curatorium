package com.sdv.kit.server.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record SubjectDto(@NotNull Long id,
                         @NotNull String name) implements Serializable {
}