package com.sdv.kit.server.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

public record MarkCreationDto(@NotNull Long studentId,
                              @NotNull Long secondLvlStatementId,
                              @NotNull @PositiveOrZero Integer value,
                              @NotNull Integer markType) implements Serializable {
}