package com.sdv.kit.server.dto.student;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record StudentDto(@NotNull Long id,
                         @NotNull String name) implements Serializable {
}