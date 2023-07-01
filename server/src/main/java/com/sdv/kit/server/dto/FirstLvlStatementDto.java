package com.sdv.kit.server.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record FirstLvlStatementDto(@NotNull Long id,
                                   @NotNull String name) implements Serializable {
}