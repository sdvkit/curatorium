package com.sdv.kit.server.dto;

import com.sdv.kit.server.model.MarkType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

public record MarkDto(@NotNull Long id,
                      @NotNull Long secondLvlStatementId,
                      @NotNull @PositiveOrZero Integer value,
                      @NotNull MarkType markType) implements Serializable {
}