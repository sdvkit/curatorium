package com.sdv.kit.server.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record SecondLvlStatementDto(@NotNull Long id) implements Serializable {
}