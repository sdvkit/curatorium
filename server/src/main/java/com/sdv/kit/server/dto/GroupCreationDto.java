package com.sdv.kit.server.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record GroupCreationDto(@NotNull @Size(min = 4, max = 15) String name,
                               @NotNull @Positive Integer groupYear) implements Serializable {
}