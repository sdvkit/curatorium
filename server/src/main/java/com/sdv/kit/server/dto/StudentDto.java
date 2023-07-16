package com.sdv.kit.server.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

public record StudentDto(@NotNull Long id,
                         @NotNull String name,
                         @NotNull List<MarkDto> marks) implements Serializable {
}