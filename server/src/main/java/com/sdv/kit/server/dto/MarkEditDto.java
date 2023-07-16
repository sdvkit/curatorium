package com.sdv.kit.server.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

public record MarkEditDto(@NotNull @PositiveOrZero Integer value) implements Serializable {
}