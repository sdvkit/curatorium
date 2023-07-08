package com.sdv.kit.server.dto.mark;

import com.sdv.kit.server.model.enumerated.MarkType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

public record MarkCreationDto(@NotNull @PositiveOrZero Integer value,
                              @NotNull MarkType markType) implements Serializable {
}