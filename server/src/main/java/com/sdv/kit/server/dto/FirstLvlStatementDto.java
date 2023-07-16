package com.sdv.kit.server.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

public record FirstLvlStatementDto(@NotNull Long id,
                                   @NotNull String name,
                                   @NotNull Long groupId,
                                   @NotNull List<SecondLvlStatementDto> secondLvlStatements) implements Serializable {
}